package com.example.oroscorp_adm.repository

import com.example.oroscorp_adm.model.*
import com.example.oroscorp_adm.service.*
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Response

/**
 * Todas as consultas são REAIS. Falhas HTTP / API são propagadas, nunca
 * substituídas por dados falsos ou por listas vazias fingindo sucesso.
 */
class OroscorpRepository(
    private val api: RawApiService = RetrofitFactory().getRawService(),
    private val gson: Gson = Gson()
) {
    private fun checked(result: Response<JsonElement>): JsonElement {
        if (!result.isSuccessful) {
            val detail = result.errorBody()?.string()?.take(300).orEmpty()
            throw IllegalStateException("HTTP ${result.code()}: ${detail.ifBlank { result.message() }}")
        }
        val json = result.body() ?: throw IllegalStateException("API retornou corpo vazio")
        if (!json.isJsonObject) return json
        val obj = json.asJsonObject
        if (obj.has("status") && !obj.get("status").isJsonNull && !obj.get("status").asBoolean) {
            throw IllegalStateException(obj.get("message")?.takeIf { !it.isJsonNull }?.asString ?: "Requisição rejeitada")
        }
        return if (obj.has("response")) obj.get("response") else json
    }

    private fun asArray(json: JsonElement, path: String): JsonArray {
        if (json.isJsonArray) return json.asJsonArray
        if (json.isJsonObject) {
            val obj = json.asJsonObject
            for (key in listOf("data", "items", "content", "results", "response")) {
                if (obj.has(key) && obj.get(key).isJsonArray) return obj.getAsJsonArray(key)
            }
        }
        throw IllegalStateException("Formato da lista em $path incompatível com a API: $json")
    }

    private suspend fun <T : Any> list(path: String, cls: Class<T>): List<T> {
        val data = checked(api.get(path))
        return asArray(data, path).map { gson.fromJson(it, cls) }
    }

    suspend fun setores(): List<Setor> = list(ApiPaths.SETOR, Setor::class.java)
    suspend fun cameras(): List<Camera> = list(ApiPaths.CAMERA, Camera::class.java)
    suspend fun funcionarios(): List<Funcionario> = list(ApiPaths.FUNCIONARIO, Funcionario::class.java)
    suspend fun cargos(): List<Cargo> = list(ApiPaths.CARGO, Cargo::class.java)
    suspend fun ocorrencias(): List<Ocorrencia> = list(ApiPaths.OCORRENCIA, Ocorrencia::class.java)
    suspend fun motivos(): List<MotivoOcorrencia> = list(ApiPaths.MOTIVO_OCORRENCIA, MotivoOcorrencia::class.java)
    suspend fun epis(): List<Epi> = list(ApiPaths.EPI, Epi::class.java)
    suspend fun statusCameras(): List<StatusCamera> = list(ApiPaths.STATUS_CAMERA, StatusCamera::class.java)
    suspend fun statusOcorrencias(): List<StatusOcorrencia> = list(ApiPaths.STATUS_OCORRENCIA, StatusOcorrencia::class.java)
    suspend fun statusFuncionarios(): List<StatusFuncionario> = list(ApiPaths.STATUS_FUNCIONARIO, StatusFuncionario::class.java)

    suspend fun setor(id: Int): Setor {
        val data = checked(api.get("${ApiPaths.SETOR}/$id"))
        if (data.isJsonNull) throw IllegalStateException("Setor não encontrado")
        return gson.fromJson(data, Setor::class.java)
    }

    /** Tentativa de login com caminho/JSON provisórios; confira no Postman. */
    suspend fun login(email: String, senha: String): Usuario? {
        require(email.isNotBlank() && senha.isNotBlank()) { "Informe e-mail e senha" }
        val body = JsonObject().apply {
            addProperty("email", email.trim())
            addProperty("senha", senha)
        }
        val rawResponse = api.post(ApiPaths.LOGIN, body)
        val result = checked(rawResponse)
        // Não autenticar por simples HTTP 200 se a API devolveu erro lógico.
        if (result.isJsonNull) throw IllegalStateException("Login sem confirmação da API")
        if (result.isJsonObject) {
            val response = result.asJsonObject
            val root = rawResponse.body()?.takeIf { it.isJsonObject }?.asJsonObject
            val token = response.get("token")?.takeIf { !it.isJsonNull }?.asString
                ?: response.get("access_token")?.takeIf { !it.isJsonNull }?.asString
                ?: root?.get("token")?.takeIf { !it.isJsonNull }?.asString
                ?: root?.get("access_token")?.takeIf { !it.isJsonNull }?.asString
            SessionAuth.setToken(token)
            val person = if (response.has("usuario") && response.get("usuario").isJsonObject)
                response.getAsJsonObject("usuario") else response
            if (person.has("id") && person.has("nome") && person.has("email"))
                return gson.fromJson<Usuario>(person, Usuario::class.java).copy(senha = "")
        }
        return null
    }

    /**
     * Não se conhece ainda o JSON exato para POST /ocorrencia.
     * O código NÃO dispara um cadastro parcial que poderia corromper registros.
     * A assinatura é mantida para ligação do formulário assim que vier o contrato.
     */
    suspend fun cadastrarOcorrencia(
        idSetor: Int, idMotivo: Int, descricao: String,
        idFuncionario: Int?, idCamera: Int?, idEpi: Int?, prioridade: String
    ) {
        throw UnsupportedOperationException(
            "Cadastro aguardando contrato POST /ocorrencia no Postman (campos obrigatórios, status e evidência)."
        )
    }
}

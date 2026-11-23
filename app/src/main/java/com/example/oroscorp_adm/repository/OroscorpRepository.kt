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
            for (key in listOf("dados", "data", "items", "content", "results", "response")) {
                if (obj.has(key) && obj.get(key).isJsonArray) return obj.getAsJsonArray(key)
            }
        }
        throw IllegalStateException("Formato da lista em $path incompatível com a API: $json")
    }

    private suspend fun <T : Any> list(path: String, cls: Class<T>): List<T> {
        val response = api.get(path)
        if (response.code() == 404 && response.errorBody()?.string()?.contains("Não foram encontrados dados para retorno.") == true) return emptyList()
        val data = checked(response)
        return asArray(data, path).map { gson.fromJson(normalizar(it), cls) }
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
        return gson.fromJson(normalizar(data.asJsonObject.get("dados") ?: data), Setor::class.java)
    }

    private fun objeto(value: JsonElement?): JsonObject? = when {
        value == null || value.isJsonNull -> null
        value.isJsonObject -> value.asJsonObject
        value.isJsonArray -> value.asJsonArray.firstOrNull()?.let { objeto(it) }
        else -> null
    }

    private fun normalizar(value: JsonElement): JsonElement {
        val obj = objeto(value)?.deepCopy() ?: return value
        for (key in listOf("setor", "cargo", "status_camera", "status_setor", "status_funcionario", "motivo_ocorrencia", "status_ocorrencia", "funcionario", "origem", "prioridade", "ocorrencia", "camera", "epi", "responsavel")) {
            val nested = objeto(obj.get(key)) ?: continue
            val id = nested.get("id") ?: continue
            val target = if (key == "responsavel") "id_usuario_registro" else "id_$key"
            if (!obj.has(target)) obj.add(target, id)
            val label = nested.get("nome") ?: nested.get("status") ?: nested.get(key)
            if (label != null) obj.add("nome_$key", label)
        }
        return obj
    }

    private fun body(vararg fields: Pair<String, String>) = JsonObject().apply {
        fields.forEach { (key, value) -> addProperty(key, value) }
    }

    suspend fun login(email: String, senha: String): Usuario {
        SessionAuth.clear()
        val result = checked(api.post(ApiPaths.LOGIN, body("email" to email.trim(), "senha" to senha))).asJsonObject
        val token = result.get("token")?.asString?.takeIf { it.isNotBlank() } ?: error("Token ausente")
        val person = objeto(result.get("dados")) ?: error("Usuário ausente")
        require((person.get("id")?.asInt ?: 0) > 0) { "Usuário inválido" }
        require(person.get("nivel_acesso")?.asString.equals("ADM", true)) { "Acesso exclusivo de administrador" }
        val user = Usuario(
            id = person.get("id").asInt,
            nome = person.get("nome")?.takeUnless { it.isJsonNull }?.asString ?: error("Nome ausente"),
            foto = person.get("foto")?.takeUnless { it.isJsonNull }?.asString.orEmpty(),
            email = person.get("email")?.takeUnless { it.isJsonNull }?.asString ?: error("E-mail ausente"),
            senha = "", data_nascimento = person.get("data_nascimento")?.takeUnless { it.isJsonNull }?.asString.orEmpty(),
            id_nivel_acesso = person.get("id_nivel_acesso")?.takeUnless { it.isJsonNull }?.asInt ?: 0,
            id_status_conta = person.get("id_status_conta")?.takeUnless { it.isJsonNull }?.asInt ?: 0
        )
        SessionAuth.setToken(token)
        return user
    }

    suspend fun solicitarSenha(email: String) {
        checked(api.post("trocar-senha/solicitar", body("email" to email.trim())))
    }
    suspend fun validarCodigo(email: String, codigo: String): String {
        require(codigo.matches(Regex("[0-9]{6}"))) { "Informe os seis dígitos" }
        return checked(api.post("trocar-senha/validar", body("email" to email, "codigo" to codigo))).asJsonObject
            .get("token_redefinicao")?.asString ?: error("Token de redefinição ausente")
    }
    suspend fun redefinirSenha(token: String, senha: String) {
        require(senha.length in 8..72) { "Senha deve ter entre 8 e 72 caracteres" }
        checked(api.post("trocar-senha/redefinir", body("token" to token, "senha" to senha)))
    }
    suspend fun evidencias(): List<OcorrenciaCamera> = list(ApiPaths.OCORRENCIA_CAMERA, OcorrenciaCamera::class.java)
    suspend fun ocorrenciaEpis(): List<OcorrenciaEpi> = list(ApiPaths.OCORRENCIA_EPI, OcorrenciaEpi::class.java)
    suspend fun cadastrarOcorrencia(idSetor: Int, idMotivo: Int, descricao: String,
        idFuncionario: Int?, idCamera: Int?, idEpi: Int?, prioridade: String, idUsuario: Int, evidencia: String?) {
        require(idCamera == null && evidencia == null) { "Envio de evidência requer upload de imagem no servidor; retire a câmera e a imagem para cadastrar sem evidência." }
        require(idFuncionario != null) { "Identifique o funcionário" }
        val status = statusOcorrencias().firstOrNull { it.status.equals("Aberta", true) } ?: error("Status Aberta ausente")
        suspend fun dominio(path: String, key: String, nome: String): Int {
            return asArray(checked(api.get(path)), path).map { it.asJsonObject }.firstOrNull { it.get(key)?.asString.equals(nome, true) }?.get("id")?.asInt ?: error("$nome não disponível em $path")
        }
        val origem = dominio(ApiPaths.ORIGEM, "origem", "MANUAL")
        val prio = dominio(ApiPaths.PRIORIDADE, "prioridade", prioridade)
        val payload = JsonObject().apply {
            addProperty("descricao", descricao); addProperty("id_setor", idSetor); addProperty("id_motivo_ocorrencia", idMotivo)
            addProperty("id_status_ocorrencia", status.id); addProperty("id_funcionario", idFuncionario)
            addProperty("id_usuario_registro", idUsuario); addProperty("id_origem", origem); addProperty("id_prioridade", prio)
            add("id_epi", JsonArray().apply { if (idEpi != null) add(JsonObject().apply { addProperty("id", idEpi) }) })
            add("cameras", JsonArray())
        }
        checked(api.post(ApiPaths.OCORRENCIA, payload))
    }
}

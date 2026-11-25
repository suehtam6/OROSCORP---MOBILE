
package com.example.oroscorp_adm.repository

import com.example.oroscorp_adm.model.*
import com.example.oroscorp_adm.service.*

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser

import retrofit2.Response

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class OroscorpRepository(
    private val api: RawApiService =
        RetrofitFactory().getRawService(),

    private val gson: Gson = Gson()
) {

    private val formatoDataSql =
        DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss"
        )

    private val fusoServidor =
        ZoneOffset.ofHours(-3)

    private fun checked(
        result: Response<JsonElement>
    ): JsonElement {

        if (!result.isSuccessful) {

            val detail = result.errorBody()
                ?.string()
                ?.take(300)
                .orEmpty()

            throw IllegalStateException(
                "HTTP ${result.code()}: ${
                    detail.ifBlank {
                        result.message()
                    }
                }"
            )
        }

        val json = result.body()
            ?: throw IllegalStateException(
                "API retornou corpo vazio"
            )

        if (!json.isJsonObject) return json

        val obj = json.asJsonObject

        if (
            obj.has("status") &&
            !obj.get("status").isJsonNull &&
            !obj.get("status").asBoolean
        ) {

            throw IllegalStateException(
                obj.get("message")
                    ?.takeIf { !it.isJsonNull }
                    ?.asString
                    ?: "Requisição rejeitada"
            )
        }

        return if (obj.has("response")) {
            obj.get("response")
        } else {
            json
        }
    }

    private fun asArray(
        json: JsonElement,
        path: String
    ): JsonArray {

        if (json.isJsonArray) {
            return json.asJsonArray
        }

        if (json.isJsonObject) {

            val obj = json.asJsonObject

            for (
            key in listOf(
                "dados",
                "data",
                "items",
                "content",
                "results",
                "response"
            )
            ) {

                if (
                    obj.has(key) &&
                    obj.get(key).isJsonArray
                ) {
                    return obj.getAsJsonArray(key)
                }
            }
        }

        throw IllegalStateException(
            "Formato da lista em $path incompatível com a API: $json"
        )
    }

    private suspend fun <T : Any> list(
        path: String,
        cls: Class<T>
    ): List<T> {

        val response = api.get(path)

        if (
            response.code() == 404 &&
            response.errorBody()
                ?.string()
                ?.contains(
                    "Não foram encontrados dados para retorno."
                ) == true
        ) {
            return emptyList()
        }

        val data = checked(response)

        return asArray(data, path).map {

            gson.fromJson(
                normalizar(it),
                cls
            )
        }
    }

    // CONSULTAS DA API

    suspend fun setores(): List<Setor> =
        list(
            ApiPaths.SETOR,
            Setor::class.java
        )

    suspend fun cameras(): List<Camera> =
        list(
            ApiPaths.CAMERA,
            Camera::class.java
        )

    suspend fun funcionarios(): List<Funcionario> =
        list(
            ApiPaths.FUNCIONARIO,
            Funcionario::class.java
        )

    suspend fun cargos(): List<Cargo> =
        list(
            ApiPaths.CARGO,
            Cargo::class.java
        )

    suspend fun ocorrencias(): List<Ocorrencia> =
        list(
            ApiPaths.OCORRENCIA,
            Ocorrencia::class.java
        )

    suspend fun motivos(): List<MotivoOcorrencia> =
        list(
            ApiPaths.MOTIVO_OCORRENCIA,
            MotivoOcorrencia::class.java
        )

    suspend fun epis(): List<Epi> =
        list(
            ApiPaths.EPI,
            Epi::class.java
        )

    suspend fun statusCameras(): List<StatusCamera> =
        list(
            ApiPaths.STATUS_CAMERA,
            StatusCamera::class.java
        )

    suspend fun statusOcorrencias(): List<StatusOcorrencia> =
        list(
            ApiPaths.STATUS_OCORRENCIA,
            StatusOcorrencia::class.java
        )

    suspend fun statusFuncionarios(): List<StatusFuncionario> =
        list(
            ApiPaths.STATUS_FUNCIONARIO,
            StatusFuncionario::class.java
        )

    suspend fun setor(id: Int): Setor {

        val data = checked(
            api.get("${ApiPaths.SETOR}/$id")
        )

        if (data.isJsonNull) {

            throw IllegalStateException(
                "Setor não encontrado"
            )
        }

        return gson.fromJson(

            normalizar(
                data.asJsonObject.get("dados")
                    ?: data
            ),

            Setor::class.java
        )
    }

    // TRATAMENTO DOS OBJETOS

    private fun objeto(
        value: JsonElement?
    ): JsonObject? = when {

        value == null ||
                value.isJsonNull -> null

        value.isJsonObject ->
            value.asJsonObject

        value.isJsonArray ->
            value.asJsonArray
                .firstOrNull()
                ?.let {
                    objeto(it)
                }

        else -> null
    }

    private fun normalizar(
        value: JsonElement
    ): JsonElement {

        val obj = objeto(value)
            ?.deepCopy()
            ?: return value

        for (
        key in listOf(
            "setor",
            "cargo",
            "status_camera",
            "status_setor",
            "status_funcionario",
            "motivo_ocorrencia",
            "status_ocorrencia",
            "funcionario",
            "origem",
            "prioridade",
            "ocorrencia",
            "camera",
            "epi",
            "responsavel"
        )
        ) {

            val nested = objeto(
                obj.get(key)
            ) ?: continue

            val id = nested.get("id")
                ?: continue

            val target = if (
                key == "responsavel"
            ) {
                "id_usuario_registro"
            } else {
                "id_$key"
            }

            if (!obj.has(target)) {

                obj.add(
                    target,
                    id
                )
            }

            val label =
                nested.get("nome")
                    ?: nested.get("status")
                    ?: nested.get(key)

            if (label != null) {

                obj.add(
                    "nome_$key",
                    label
                )
            }
        }

        return obj
    }

    private fun body(
        vararg fields: Pair<String, String>
    ) = JsonObject().apply {

        fields.forEach { (key, value) ->

            addProperty(
                key,
                value
            )
        }
    }

    // TRATAMENTO DA DATA E HORA

    private fun preservarDataHora(
        dataOriginal: String
    ): String {

        val data = dataOriginal.trim()

        require(data.isNotBlank()) {
            "A data da evidência não foi informada pela API."
        }

        try {

            // Quando o servidor retorna uma data
            // ISO 8601 com UTC ou offset explícito.

            if (
                data.endsWith("Z", true) ||
                Regex(
                    "[+-]\\d{2}:\\d{2}$"
                ).containsMatchIn(data)
            ) {

                return OffsetDateTime
                    .parse(data.replace(' ', 'T'))
                    .withOffsetSameInstant(
                        fusoServidor
                    )
                    .format(formatoDataSql)
            }

            // Quando o backend já retorna o horário
            // local, não aplica nenhuma conversão.

            return LocalDateTime
                .parse(
                    data.replace(' ', 'T'),
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                )
                .format(formatoDataSql)

        } catch (e: Exception) {

            throw IllegalStateException(
                "Formato de data inválido retornado pela API: $data",
                e
            )
        }
    }

    // LOGIN

    suspend fun login(
        email: String,
        senha: String
    ): Usuario {

        SessionAuth.clear()

        val result = checked(

            api.post(

                ApiPaths.LOGIN,

                body(
                    "email" to email.trim(),
                    "senha" to senha
                )
            )

        ).asJsonObject

        val token = result.get("token")
            ?.asString
            ?.takeIf {
                it.isNotBlank()
            }
            ?: error("Token ausente")

        val person = objeto(
            result.get("dados")
        ) ?: error("Usuário ausente")

        require(
            (person.get("id")?.asInt ?: 0) > 0
        ) {
            "Usuário inválido"
        }

        require(
            person.get("nivel_acesso")
                ?.asString
                .equals("ADM", true)
        ) {
            "Acesso exclusivo de administrador"
        }

        val user = Usuario(

            id = person.get("id").asInt,

            nome = person.get("nome")
                ?.takeUnless {
                    it.isJsonNull
                }
                ?.asString
                ?: error("Nome ausente"),

            foto = person.get("foto")
                ?.takeUnless {
                    it.isJsonNull
                }
                ?.asString
                .orEmpty(),

            email = person.get("email")
                ?.takeUnless {
                    it.isJsonNull
                }
                ?.asString
                ?: error("E-mail ausente"),

            senha = "",

            data_nascimento = person
                .get("data_nascimento")
                ?.takeUnless {
                    it.isJsonNull
                }
                ?.asString
                .orEmpty(),

            id_nivel_acesso = person
                .get("id_nivel_acesso")
                ?.takeUnless {
                    it.isJsonNull
                }
                ?.asInt ?: 0,

            id_status_conta = person
                .get("id_status_conta")
                ?.takeUnless {
                    it.isJsonNull
                }
                ?.asInt ?: 0
        )

        SessionAuth.setToken(token)

        return user
    }

    // RECUPERAÇÃO DE SENHA

    suspend fun solicitarSenha(
        email: String
    ) {

        checked(

            api.post(

                "trocar-senha/solicitar",

                body(
                    "email" to email.trim()
                )
            )
        )
    }

    suspend fun validarCodigo(
        email: String,
        codigo: String
    ): String {

        require(
            codigo.matches(
                Regex("[0-9]{6}")
            )
        ) {
            "Informe os seis dígitos"
        }

        return checked(

            api.post(

                "trocar-senha/validar",

                body(
                    "email" to email,
                    "codigo" to codigo
                )
            )

        ).asJsonObject
            .get("token_redefinicao")
            ?.asString
            ?: error(
                "Token de redefinição ausente"
            )
    }

    suspend fun redefinirSenha(
        token: String,
        senha: String
    ) {

        require(
            senha.length in 8..72
        ) {
            "Senha deve ter entre 8 e 72 caracteres"
        }

        checked(

            api.post(

                "trocar-senha/redefinir",

                body(
                    "token" to token,
                    "senha" to senha
                )
            )
        )
    }

    // EVIDÊNCIAS E EPIS

    suspend fun evidencias(): List<OcorrenciaCamera> =
        list(
            ApiPaths.OCORRENCIA_CAMERA,
            OcorrenciaCamera::class.java
        )

    suspend fun ocorrenciaEpis(): List<OcorrenciaEpi> =
        list(
            ApiPaths.OCORRENCIA_EPI,
            OcorrenciaEpi::class.java
        )

    // CADASTRAR OCORRÊNCIA

    suspend fun cadastrarOcorrencia(

        idSetor: Int,

        idMotivo: Int,

        descricao: String,

        idFuncionario: Int?,

        idCamera: Int?,

        idEpi: Int?,

        prioridade: String,

        idUsuario: Int,

        evidencia: String?
    ) {

        require(
            idCamera == null &&
                    evidencia == null
        ) {
            "Envio de evidência requer upload de imagem no servidor; retire a câmera e a imagem para cadastrar sem evidência."
        }

        require(
            idFuncionario != null
        ) {
            "Identifique o funcionário"
        }

        val status = statusOcorrencias()
            .firstOrNull {

                it.status.equals(
                    "Aberta",
                    true
                )

            } ?: error(
            "Status Aberta ausente"
        )

        suspend fun dominio(

            path: String,

            key: String,

            nome: String

        ): Int {

            return asArray(
                checked(api.get(path)),
                path
            )

                .map {
                    it.asJsonObject
                }

                .firstOrNull {

                    it.get(key)
                        ?.asString
                        .equals(nome, true)
                }

                ?.get("id")

                ?.asInt

                ?: error(
                    "$nome não disponível em $path"
                )
        }

        val origem = dominio(

            ApiPaths.ORIGEM,

            "origem",

            "MANUAL"
        )

        val prio = dominio(

            ApiPaths.PRIORIDADE,

            "prioridade",

            prioridade
        )

        val payload = JsonObject().apply {

            addProperty(
                "descricao",
                descricao
            )

            addProperty(
                "id_setor",
                idSetor
            )

            addProperty(
                "id_motivo_ocorrencia",
                idMotivo
            )

            addProperty(
                "id_status_ocorrencia",
                status.id
            )

            addProperty(
                "id_funcionario",
                idFuncionario
            )

            addProperty(
                "id_usuario_registro",
                idUsuario
            )

            addProperty(
                "id_origem",
                origem
            )

            addProperty(
                "id_prioridade",
                prio
            )

            add(

                "id_epi",

                JsonArray().apply {

                    if (idEpi != null) {

                        add(

                            JsonObject().apply {

                                addProperty(
                                    "id",
                                    idEpi
                                )
                            }
                        )
                    }
                }
            )

            add(
                "cameras",
                JsonArray()
            )
        }

        checked(

            api.post(

                ApiPaths.OCORRENCIA,

                payload
            )
        )
    }

    // FECHAR OU REABRIR OCORRÊNCIA
    // Utiliza exclusivamente a rota existente.

    suspend fun alterarStatusOcorrencia(
        id: Int
    ) {

        require(id > 0) {
            "Ocorrência inválida."
        }

        val path =
            "${ApiPaths.OCORRENCIA}/$id"

        val atual = asArray(

            checked(
                api.get(path)
            ),

            path

        ).map {

            normalizar(it).asJsonObject

        }.singleOrNull {

            it.get("id")?.asInt == id

        } ?: error(
            "Ocorrência não encontrada."
        )

        fun campo(

            obj: JsonObject,

            nome: String

        ): JsonElement {

            return obj.get(nome)
                ?: error(
                    "A API não retornou $nome. Atualização cancelada."
                )
        }

        fun idObrigatorio(

            obj: JsonObject,

            nome: String

        ): Int {

            val valor = campo(
                obj,
                nome
            )

            require(
                !valor.isJsonNull &&
                        valor.asInt > 0
            ) {
                "ID inválido: $nome"
            }

            return valor.asInt
        }

        val statusAtual = idObrigatorio(

            atual,

            "id_status_ocorrencia"
        )

        val status = statusOcorrencias()

        val nomeAtual = status.singleOrNull {

            it.id == statusAtual

        }?.status ?: error(
            "Status atual não encontrado na API."
        )

        val destino = if (

            nomeAtual.equals(
                "Resolvida",
                true
            ) ||

            nomeAtual.equals(
                "Fechada",
                true
            )

        ) {

            "Aberta"

        } else {

            "Resolvida"
        }

        val novoStatus = status.singleOrNull {

            it.status.equals(
                destino,
                true
            )

        } ?: error(
            "Status $destino não encontrado na API."
        )

        // BUSCAR RELACIONAMENTOS REAIS

        suspend fun relacionamentos(

            rota: String

        ): List<JsonObject> {

            val resposta = api.get(rota)

            if (resposta.code() == 404) {

                val erro = resposta.errorBody()
                    ?.string()
                    .orEmpty()

                if (
                    erro.contains(
                        "Não foram encontrados dados para retorno."
                    )
                ) {

                    return emptyList()
                }

                error(
                    "HTTP 404 ao consultar $rota: $erro"
                )
            }

            return asArray(

                checked(resposta),

                rota

            ).map {

                normalizar(it).asJsonObject

            }.filter {

                idObrigatorio(
                    it,
                    "id_ocorrencia"
                ) == id
            }
        }

        val descricao = campo(

            atual,

            "descricao"

        ).asString

        require(

            !descricao.contains('\'') &&

                    !descricao.contains('\\') &&

                    !descricao.contains('"')

        ) {
            "O backend atual não preserva esta descrição com segurança. Atualização cancelada."
        }

        // BUSCAR DADOS ANTES DA ATUALIZAÇÃO

        val episOriginais = relacionamentos(
            ApiPaths.OCORRENCIA_EPI
        )

        val camerasOriginais = relacionamentos(
            ApiPaths.OCORRENCIA_CAMERA
        )

        // PRESERVAR OS HORÁRIOS ORIGINAIS
        // Não utilizar o horário atual do aparelho.

        val horariosOriginais =
            camerasOriginais.map { camera ->

                preservarDataHora(

                    campo(
                        camera,
                        "data_hora"
                    ).asString
                )
            }.sorted()

        // MONTAR CORPO DA REQUISIÇÃO

        val payload = JsonObject().apply {

            addProperty(
                "descricao",
                descricao
            )

            for (

            chave in listOf(

                "id_setor",

                "id_motivo_ocorrencia",

                "id_origem",

                "id_prioridade"
            )
            ) {

                addProperty(

                    chave,

                    idObrigatorio(
                        atual,
                        chave
                    )
                )
            }

            addProperty(

                "id_status_ocorrencia",

                novoStatus.id
            )

            add(

                "id_funcionario",

                campo(
                    atual,
                    "id_funcionario"
                )
            )

            add(

                "id_usuario_registro",

                campo(
                    atual,
                    "id_usuario_registro"
                )
            )
        }

        // PRESERVAR EPIS

        payload.add(

            "id_epi",

            JsonArray().apply {

                episOriginais.forEach { epi ->

                    add(

                        JsonObject().apply {

                            addProperty(

                                "id",

                                idObrigatorio(
                                    epi,
                                    "id_epi"
                                )
                            )
                        }
                    )
                }
            }
        )

        // PRESERVAR CÂMERAS E EVIDÊNCIAS

        payload.add(

            "cameras",

            JsonArray().apply {

                camerasOriginais.forEach { camera ->

                    add(

                        JsonObject().apply {

                            add(

                                "id_camera",

                                campo(
                                    camera,
                                    "id_camera"
                                )
                            )

                            val foto = campo(

                                camera,

                                "foto"
                            )

                            require(

                                !foto.isJsonNull &&

                                        foto.asString.isNotBlank()

                            ) {
                                "Foto da evidência indisponível."
                            }

                            add(
                                "foto",
                                foto
                            )

                            add(

                                "confianca_ia",

                                campo(
                                    camera,
                                    "confianca_ia"
                                )
                            )

                            // CORREÇÃO DO HORÁRIO

                            val dataOriginal = campo(

                                camera,

                                "data_hora"

                            ).asString

                            val dataPreservada =
                                preservarDataHora(
                                    dataOriginal
                                )

                            addProperty(

                                "data_hora",

                                dataPreservada
                            )
                        }
                    )
                }
            }
        )

        // ATUALIZAR OCORRÊNCIA

        val resposta = api.put(

            path,

            payload
        )

        val codigo = resposta.code()

        checked(resposta)

        check(codigo == 200) {

            "A API retornou HTTP $codigo. " +
                    "Confira a ocorrência antes de tentar novamente."
        }

        // VERIFICAR SE O BACKEND PRESERVOU
        // O HORÁRIO E AS EVIDÊNCIAS.

        val camerasAtualizadas = relacionamentos(
            ApiPaths.OCORRENCIA_CAMERA
        )

        val horariosAtualizados =
            camerasAtualizadas.map { camera ->

                preservarDataHora(

                    campo(
                        camera,
                        "data_hora"
                    ).asString
                )
            }.sorted()

        check(
            horariosOriginais == horariosAtualizados
        ) {

            "O status foi enviado, mas o servidor " +
                    "alterou o horário ou a quantidade das " +
                    "evidências. Não repita a operação " +
                    "antes de verificar os registros."
        }
    }
}

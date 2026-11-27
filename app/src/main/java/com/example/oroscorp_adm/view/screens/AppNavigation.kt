
package com.example.oroscorp_adm.view.screens

import com.example.oroscorp_adm.ui.theme.TemaGlobal
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.oroscorp_adm.service.ApiConfig
import java.time.Instant
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

import com.example.oroscorp_adm.model.*
import com.example.oroscorp_adm.repository.OroscorpRepository
import com.example.oroscorp_adm.service.SessionAuth

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch


private val CorFundoPopup
    get() = TemaGlobal.cor(Color(0xFF101315), Color(0xFFFFFFFF))
private val CorLaranjaPopup
    get() = TemaGlobal.cor(Color(0xFFFF9500), Color(0xFFB85A00))
private val CorTextoPopup
    get() = TemaGlobal.cor(Color(0xFFF5F5F5), Color(0xFF191C1F))
private val CorTextoSecundarioPopup
    get() = TemaGlobal.cor(Color(0xFFB0B0B0), Color(0xFF60656B))

@Composable
private fun PopupErroOroscorp(
    mensagem: String,
    onFechar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onFechar,

        containerColor = CorFundoPopup,
        shape = RoundedCornerShape(16.dp),

        icon = {
            Text(
                text = "⚠",
                color = CorLaranjaPopup,
                fontSize = 34.sp
            )
        },

        title = {
            Text(
                text = "Ocorreu um erro",
                color = CorLaranjaPopup,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
        },

        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Não foi possível concluir a operação.",
                    color = CorTextoPopup,
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = mensagem,
                    color = CorTextoSecundarioPopup,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            }
        },

        confirmButton = {
            Button(
                onClick = onFechar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CorLaranjaPopup,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(9.dp)
            ) {
                Text(
                    text = "Entendi",
                    fontWeight = FontWeight.Bold
                )
            }
        },

        modifier = Modifier.border(
            width = 1.dp,
            color = CorLaranjaPopup,
            shape = RoundedCornerShape(16.dp)
        )
    )
}

@Composable
fun AppNavigation() {

    val repo = remember { OroscorpRepository() }
    val scope = rememberCoroutineScope()
    val contexto = LocalContext.current.applicationContext
    val datasLocais = remember(contexto) {
        contexto.getSharedPreferences(
            "oroscorp_datas_" + ApiConfig.BASE_URL.hashCode(),
            android.content.Context.MODE_PRIVATE
        )
    }
    var datasCadastro by remember(datasLocais) {
        val mapa: Map<Int, Instant> = buildMap<Int, Instant> {
            datasLocais.all.forEach { (chave, valor) ->
                val id = chave.toIntOrNull() ?: return@forEach
                val texto = valor as? String ?: return@forEach
                val instante = runCatching { Instant.parse(texto) }
                    .getOrNull() ?: return@forEach
                put(id, instante)
            }
        }
        mutableStateOf<Map<Int, Instant>>(mapa)
    }

    var tela by remember { mutableStateOf("login") }
    var recarregar by remember { mutableIntStateOf(0) }
    var carregando by remember { mutableStateOf(false) }

    // ERROS

    var mensagemErro by remember {
        mutableStateOf<String?>(null)
    }

    var erroLogin by remember {
        mutableStateOf<String?>(null)
    }

    var erroCadastro by remember {
        mutableStateOf<String?>(null)
    }

    var erroSenha by remember {
        mutableStateOf<String?>(null)
    }

    // RECUPERAÇÃO DE SENHA

    var emailRecuperacao by remember {
        mutableStateOf("")
    }

    var tokenRedefinicao by remember {
        mutableStateOf("")
    }

    // SESSÃO

    var usuarioAtual by remember {
        mutableStateOf<Usuario?>(null)
    }

    var cameraSelecionada by remember {
        mutableIntStateOf(0)
    }

    var setorSelecionado by remember {
        mutableIntStateOf(0)
    }

    var ocorrenciaSelecionada by remember {
        mutableStateOf<DetalhesOcorrencia?>(null)
    }

    // DADOS DA API

    var setores by remember {
        mutableStateOf<List<Setor>>(emptyList())
    }

    var cameras by remember {
        mutableStateOf<List<Camera>>(emptyList())
    }

    var funcionarios by remember {
        mutableStateOf<List<Funcionario>>(emptyList())
    }

    var cargos by remember {
        mutableStateOf<List<Cargo>>(emptyList())
    }

    var ocorrencias by remember {
        mutableStateOf<List<Ocorrencia>>(emptyList())
    }

    var motivos by remember {
        mutableStateOf<List<MotivoOcorrencia>>(emptyList())
    }

    var epis by remember {
        mutableStateOf<List<Epi>>(emptyList())
    }

    var statusCameras by remember {
        mutableStateOf<List<StatusCamera>>(emptyList())
    }

    var statusOcorrencias by remember {
        mutableStateOf<List<StatusOcorrencia>>(emptyList())
    }

    var statusFuncionarios by remember {
        mutableStateOf<List<StatusFuncionario>>(emptyList())
    }

    var evidencias by remember {
        mutableStateOf<List<OcorrenciaCamera>>(emptyList())
    }

    var vinculosEpi by remember {
        mutableStateOf<List<OcorrenciaEpi>>(emptyList())
    }

    // STATUS DAS CONSULTAS

    var setoresOk by remember {
        mutableStateOf(false)
    }

    var camerasOk by remember {
        mutableStateOf(false)
    }

    var funcionariosOk by remember {
        mutableStateOf(false)
    }

    var ocorrenciasOk by remember {
        mutableStateOf(false)
    }

    var statusCamerasOk by remember {
        mutableStateOf(false)
    }

    var statusOcorrenciasOk by remember {
        mutableStateOf(false)
    }

    var evidenciasOk by remember {
        mutableStateOf(false)
    }

    // CARREGAMENTO DE DADOS

    LaunchedEffect(tela, recarregar) {

        val telasComConsulta = setOf(
            "home",
            "setores",
            "detalhesSetor",
            "cameras",
            "camera",
            "funcionarios",
            "ocorrencias",
            "detalhesOcorrencia",
            "registrar"
        )

        if (tela !in telasComConsulta) {
            return@LaunchedEffect
        }

        carregando = true
        mensagemErro = null

        val avisos = mutableListOf<String>()

        suspend fun <T> consultar(
            nome: String,
            consulta: suspend () -> T,
            sucesso: (T) -> Unit,
            falha: () -> Unit = {}
        ) {
            try {
                sucesso(consulta())

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                falha()

                avisos.add(
                    "$nome: ${e.message ?: "Falha na comunicação com a API."}"
                )
            }
        }

        suspend fun carregarSetores() {

            consultar(
                nome = "Setores",
                consulta = repo::setores,

                sucesso = {
                    setores = it
                    setoresOk = true
                },

                falha = {
                    setores = emptyList()
                    setoresOk = false
                }
            )
        }

        suspend fun carregarCameras() {

            consultar(
                nome = "Câmeras",
                consulta = repo::cameras,

                sucesso = {
                    cameras = it
                    camerasOk = true
                },

                falha = {
                    cameras = emptyList()
                    camerasOk = false
                }
            )
        }

        suspend fun carregarFuncionarios() {

            consultar(
                nome = "Funcionários",
                consulta = repo::funcionarios,

                sucesso = {
                    funcionarios = it
                    funcionariosOk = true
                },

                falha = {
                    funcionarios = emptyList()
                    funcionariosOk = false
                }
            )
        }

        suspend fun carregarOcorrencias() {

            consultar(
                nome = "Ocorrências",
                consulta = repo::ocorrencias,

                sucesso = {
                    ocorrencias = it
                    ocorrenciasOk = true
                },

                falha = {
                    ocorrencias = emptyList()
                    ocorrenciasOk = false
                }
            )
        }

        suspend fun carregarStatusCameras() {

            consultar(
                nome = "Status das câmeras",
                consulta = repo::statusCameras,

                sucesso = {
                    statusCameras = it
                    statusCamerasOk = true
                },

                falha = {
                    statusCameras = emptyList()
                    statusCamerasOk = false
                }
            )
        }

        suspend fun carregarStatusOcorrencias() {

            consultar(
                nome = "Status das ocorrências",
                consulta = repo::statusOcorrencias,

                sucesso = {
                    statusOcorrencias = it
                    statusOcorrenciasOk = true
                },

                falha = {
                    statusOcorrencias = emptyList()
                    statusOcorrenciasOk = false
                }
            )
        }

        suspend fun carregarEvidencias() {

            consultar(
                nome = "Evidências",
                consulta = repo::evidencias,

                sucesso = {
                    evidencias = it
                    evidenciasOk = true
                },

                falha = {
                    evidencias = emptyList()
                    evidenciasOk = false
                }
            )
        }

        try {

            when (tela) {

                // DASHBOARD

                "home" -> {

                    carregarCameras()
                    carregarStatusCameras()
                    carregarOcorrencias()
                    carregarStatusOcorrencias()
                    carregarEvidencias()
                }

                // SETORES

                "setores", "detalhesSetor" -> {

                    carregarSetores()
                    carregarCameras()
                    carregarFuncionarios()
                    carregarOcorrencias()
                    carregarStatusCameras()
                    carregarStatusOcorrencias()
                    carregarEvidencias()

                    consultar(
                        nome = "Cargos",
                        consulta = repo::cargos,

                        sucesso = {
                            cargos = it
                        },

                        falha = {
                            cargos = emptyList()
                        }
                    )

                    consultar(
                        nome = "Status dos funcionários",
                        consulta = repo::statusFuncionarios,

                        sucesso = {
                            statusFuncionarios = it
                        },

                        falha = {
                            statusFuncionarios = emptyList()
                        }
                    )
                }

                // CÂMERAS

                "cameras", "camera" -> {

                    carregarSetores()
                    carregarCameras()
                    carregarStatusCameras()
                }

                // FUNCIONÁRIOS

                "funcionarios" -> {

                    carregarFuncionarios()

                    consultar(
                        nome = "Cargos",
                        consulta = repo::cargos,

                        sucesso = {
                            cargos = it
                        },

                        falha = {
                            cargos = emptyList()
                        }
                    )
                }

                // OCORRÊNCIAS

                "ocorrencias", "detalhesOcorrencia" -> {

                    carregarOcorrencias()
                    carregarStatusOcorrencias()
                    carregarEvidencias()
                    carregarSetores()

                    consultar(
                        nome = "Motivos",
                        consulta = repo::motivos,

                        sucesso = {
                            motivos = it
                        },

                        falha = {
                            motivos = emptyList()
                        }
                    )

                    consultar(
                        nome = "EPIs das ocorrências",
                        consulta = repo::ocorrenciaEpis,

                        sucesso = {
                            vinculosEpi = it
                        },

                        falha = {
                            vinculosEpi = emptyList()
                        }
                    )
                }

                // REGISTRAR OCORRÊNCIA

                "registrar" -> {

                    carregarFuncionarios()
                    carregarSetores()
                    carregarCameras()

                    consultar(
                        nome = "Motivos",
                        consulta = repo::motivos,

                        sucesso = {
                            motivos = it
                        },

                        falha = {
                            motivos = emptyList()
                        }
                    )

                    consultar(
                        nome = "EPIs",
                        consulta = repo::epis,

                        sucesso = {
                            epis = it
                        },

                        falha = {
                            epis = emptyList()
                        }
                    )
                }
            }

        } finally {

            mensagemErro = avisos
                .takeIf { it.isNotEmpty() }
                ?.joinToString("\n")

            carregando = false
        }
    }

    // SETORES

    val setorInfo: List<SetorInfo> =
        if (setoresOk) {

            setores.map { setor ->

                val ocorrenciasDoSetor = ocorrencias.filter {
                    it.id_setor == setor.id
                }

                val statusConhecidos =
                    ocorrenciasDoSetor.all { ocorrencia ->

                        statusOcorrencias.any {
                            it.id ==
                                    ocorrencia.id_status_ocorrencia
                        }
                    }

                val abertas: Int? =
                    if (
                        ocorrenciasOk &&
                        statusOcorrenciasOk &&
                        statusConhecidos
                    ) {

                        ocorrenciasDoSetor.count { ocorrencia ->

                            val status = statusOcorrencias
                                .firstOrNull {
                                    it.id ==
                                            ocorrencia.id_status_ocorrencia
                                }
                                ?.status
                                .orEmpty()

                            status.equals("Aberta", true) ||
                                    status.equals("Em aberto", true)
                        }

                    } else {

                        null
                    }

                SetorInfo(
                    id = setor.id,

                    nome = setor.nome,

                    descricao = setor.descricao,

                    funcionarios = if (funcionariosOk) {

                        funcionarios.count {
                            it.id_setor == setor.id
                        }

                    } else {

                        null
                    },

                    cameras = if (camerasOk) {

                        cameras.count {
                            it.id_setor == setor.id
                        }

                    } else {

                        null
                    },

                    abertas = abertas
                )
            }

        } else {

            emptyList()
        }

    // DETALHES DAS OCORRÊNCIAS

    val detalhesOcorrencias = ocorrencias.map { item ->

        val evidencia = evidencias.firstOrNull {
            it.id_ocorrencia == item.id
        }

        val status = statusOcorrencias.firstOrNull {
            it.id == item.id_status_ocorrencia
        }

        val motivo = motivos.firstOrNull {
            it.id == item.id_motivo_ocorrencia
        }

        val setor = setores.firstOrNull {
            it.id == item.id_setor
        }

        DetalhesOcorrencia(

            id = item.id,

            tipoIA = item.nome_origem.equals(
                "IA",
                ignoreCase = true
            ),

            origemConhecida =
                !item.nome_origem.isNullOrBlank(),

            funcionario = item.nome_funcionario,

            descricao = item.descricao.orEmpty(),

            epi = vinculosEpi
                .filter {
                    it.id_ocorrencia == item.id
                }
                .mapNotNull {
                    it.nome_epi
                }
                .joinToString(),

            camera = evidencia
                ?.nome_camera
                .orEmpty(),

            data = evidencia
                ?.dataHora
                ?.take(10)
                .orEmpty(),

            horario = evidencia
                ?.dataHora
                ?.drop(11)
                ?.take(8)
                .orEmpty(),

            confiancaIA = evidencia
                ?.confianca_ia,

            evidenciaUrl = evidencia
                ?.foto,

            status = status
                ?.status
                ?: item.nome_status_ocorrencia.orEmpty(),

            prioridade =
                item.nome_prioridade ?: "—",

            tipo = motivo
                ?.nome
                ?: item.nome_motivo_ocorrencia.orEmpty(),

            setor = setor
                ?.nome
                ?: item.nome_setor.orEmpty()
        )
    }

    // CÂMERAS POR SETOR

    val cameraSetor = cameras.map { camera ->

        val status = statusCameras.firstOrNull {
            it.id == camera.id_status_camera
        }

        CameraInfo(

            id = camera.id,

            nome = camera.nome,

            setorId = camera.id_setor,

            online = status?.status.equals(
                "online",
                ignoreCase = true
            ),

            statusConhecido = status != null
        )
    }

    // FUNCIONÁRIOS POR SETOR

    val funcionarioSetor = funcionarios.map { funcionario ->

        val status = statusFuncionarios.firstOrNull {
            it.id == funcionario.id_status_funcionario
        }

        FuncionarioInfo(

            nome = funcionario.nome,

            cargo = cargos.firstOrNull {
                it.id == funcionario.id_cargo
            }?.cargo ?: "—",

            ativo = status?.status.equals(
                "ativo",
                ignoreCase = true
            ),

            setorId = funcionario.id_setor,

            statusConhecido = status != null
        )
    }

    // OCORRÊNCIAS POR SETOR

    val ocorrenciaSetor = ocorrencias.map { ocorrencia ->

        val evidencia = evidencias.firstOrNull {
            it.id_ocorrencia == ocorrencia.id
        }

        OcorrenciaInfo(

            codigo = "#${ocorrencia.id}",

            funcionario =
                ocorrencia.nome_funcionario ?: "—",

            data = evidencia
                ?.dataHora
                ?.take(10) ?: "—",

            status = statusOcorrencias.firstOrNull {
                it.id == ocorrencia.id_status_ocorrencia
            }?.status ?: "—",

            origem =
                ocorrencia.nome_origem ?: "—",

            setorId = ocorrencia.id_setor
        )
    }

    // INDICADORES REAIS

    val camerasOnline: Int? =
        if (
            camerasOk &&
            statusCamerasOk &&
            cameras.all { camera ->

                statusCameras.any {
                    it.id == camera.id_status_camera
                }
            }
        ) {

            cameras.count { camera ->

                statusCameras.firstOrNull {
                    it.id == camera.id_status_camera
                }?.status.equals(
                    "online",
                    ignoreCase = true
                )
            }

        } else {

            null
        }

    val totalCameras: Int? =
        if (camerasOk) {

            cameras.size

        } else {

            null
        }

    val aguardandoIdentificacao: Int? =
        if (ocorrenciasOk) {

            ocorrencias.count {
                it.id_funcionario == null
            }

        } else {

            null
        }

    val resumoDashboard = DashboardOcorrencias.calcular(
        ocorrencias = ocorrencias,
        evidencias = evidencias,
        dadosDisponiveis = ocorrenciasOk && evidenciasOk,
        datasCadastro = datasCadastro
    )

    val ocorrenciasHoje = resumoDashboard.hoje
    val dadosGrafico = resumoDashboard.grafico

    // POP-UP CENTRALIZADO
    // Prioriza o erro da tela atual.

    val erroAtual: String? = when (tela) {

        "login" -> erroLogin

        "esqueceu", "codigo", "trocar" -> erroSenha

        "registrar" -> erroCadastro ?: mensagemErro

        else -> mensagemErro
    }

    fun fecharPopupErro() {

        when (tela) {

            "login" -> {
                erroLogin = null
            }

            "esqueceu", "codigo", "trocar" -> {
                erroSenha = null
            }

            "registrar" -> {
                erroCadastro = null
                mensagemErro = null
            }

            else -> {
                mensagemErro = null
            }
        }
    }

    // NAVEGAÇÃO

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TemaGlobal.cor(Color(0xFF353535), Color(0xFFF2F3F5)))
    ) {

        when (tela) {

            // LOGIN

            "login" -> LoginScreen(

                onEntrarClick = {},

                onEsqueceuSenhaClick = {

                    erroSenha = null

                    tela = "esqueceu"
                },

                onEntrarComCredenciais = { email, senha ->

                    scope.launch {

                        erroLogin = null

                        carregando = true

                        try {

                            usuarioAtual = repo.login(
                                email,
                                senha
                            )

                            tela = "home"

                        } catch (e: CancellationException) {

                            throw e

                        } catch (e: Exception) {

                            erroLogin = e.message
                                ?: "Não foi possível realizar o login."

                        } finally {

                            carregando = false
                        }
                    }
                },

                carregando = carregando,

                erro = null
            )

            // ESQUECEU SENHA

            "esqueceu" -> EsqueceuSenhaScreen(

                onBackClick = {

                    if (!carregando) {

                        erroSenha = null

                        tela = "login"
                    }
                },

                onContinueClick = {},

                onEnviarEmail = { email ->

                    scope.launch {

                        carregando = true

                        erroSenha = null

                        try {

                            repo.solicitarSenha(email)

                            emailRecuperacao = email.trim()

                            tela = "codigo"

                        } catch (e: CancellationException) {

                            throw e

                        } catch (e: Exception) {

                            erroSenha = e.message
                                ?: "Não foi possível solicitar a recuperação de senha."

                        } finally {

                            carregando = false
                        }
                    }
                },

                erro = null,

                carregando = carregando
            )

            // CÓDIGO DE RECUPERAÇÃO

            "codigo" -> RedefinirSenhaCodigo(

                onBackClick = {

                    if (!carregando) {

                        erroSenha = null

                        tela = "esqueceu"
                    }
                },

                onContinueClick = {},

                onValidarCodigo = { codigo ->

                    scope.launch {

                        carregando = true

                        erroSenha = null

                        try {

                            tokenRedefinicao = repo.validarCodigo(
                                emailRecuperacao,
                                codigo
                            )

                            tela = "trocar"

                        } catch (e: CancellationException) {

                            throw e

                        } catch (e: Exception) {

                            erroSenha = e.message
                                ?: "Não foi possível validar o código."

                        } finally {

                            carregando = false
                        }
                    }
                },

                erro = null,

                carregando = carregando
            )

            // TROCAR SENHA

            "trocar" -> TrocarSenhaScreen(

                onBackClick = {

                    if (!carregando) {

                        tokenRedefinicao = ""

                        erroSenha = null

                        tela = "esqueceu"
                    }
                },

                onRedefinirClick = {},

                onConfirmarSenha = { senha ->

                    scope.launch {

                        carregando = true

                        erroSenha = null

                        try {

                            repo.redefinirSenha(
                                tokenRedefinicao,
                                senha
                            )

                            tokenRedefinicao = ""

                            emailRecuperacao = ""

                            erroLogin = null

                            tela = "login"

                        } catch (e: CancellationException) {

                            throw e

                        } catch (e: Exception) {

                            erroSenha = e.message
                                ?: "Não foi possível redefinir a senha."

                        } finally {

                            carregando = false
                        }
                    }
                },

                erro = null,

                carregando = carregando
            )

            // DASHBOARD

            "home" -> HomeScreen(

                onDashboardClick = {

                    recarregar++
                },

                onSetoresClick = {

                    tela = "setores"
                },

                onCamerasClick = {

                    tela = "cameras"
                },

                onOcorrenciasClick = {

                    tela = "ocorrencias"
                },

                onFuncionariosClick = {

                    tela = "funcionarios"
                },

                onPerfilClick = {

                    tela = "perfil"
                },

                onSairClick = {

                    tela = "login"

                    erroLogin = null

                    usuarioAtual = null

                    mensagemErro = null

                    SessionAuth.clear()
                },

                ocorrenciasHoje = ocorrenciasHoje,

                aguardandoIdentificacao =
                    aguardandoIdentificacao,

                camerasOnline = camerasOnline,

                totalCameras = totalCameras,

                nomeUsuario =
                    usuarioAtual?.nome.orEmpty(),

                carregando = carregando,

                erro = null,

                dadosGrafico = dadosGrafico,
                ocorrenciasSemData = resumoDashboard.semData,

                onAtualizarClick = {

                    recarregar++
                }
            )

            // PERFIL

            "perfil" -> PerfilAdminScreen(

                usuario = usuarioAtual,

                onBackClick = {

                    tela = "home"
                }
            )

            // SETORES

            "setores" -> SetoresScreen(

                onVoltar = {

                    tela = "home"
                },

                onAbrirSetor = { id ->

                    setorSelecionado = id

                    tela = "detalhesSetor"
                },

                setores = setorInfo
            )

            // DETALHES DO SETOR

            "detalhesSetor" -> DetalhesSetorScreen(

                setorId = setorSelecionado,

                onVoltar = {

                    tela = "setores"
                },

                setores = setorInfo,

                cameras = cameraSetor,

                funcionarios = funcionarioSetor,

                ocorrencias = ocorrenciaSetor
            )

            // CÂMERAS

            "cameras" -> CamerasScreen(

                onBackClick = {

                    tela = "home"
                },

                cameras = cameras,

                statusCameras = statusCameras,

                onCameraClick = { indice ->

                    cameraSelecionada = indice

                    tela = "camera"
                }
            )

            // VISUALIZAÇÃO DA CÂMERA

            "camera" -> {

                val camera = cameras.getOrNull(
                    cameraSelecionada
                )

                val status = camera?.let { item ->

                    statusCameras.firstOrNull {
                        it.id == item.id_status_camera
                    }
                }

                CameraScreen(

                    nomeCamera = camera?.nome
                        ?: "Câmera indisponível",

                    videoUrl =
                        camera?.endereco_stream,

                    online = status?.status.equals(
                        "online",
                        true
                    ),

                    statusConhecido =
                        status != null,

                    onBackClick = {

                        tela = "cameras"
                    },

                    temAnterior =
                        cameraSelecionada > 0,

                    temProxima =
                        cameraSelecionada + 1 < cameras.size,

                    onAnteriorClick = {

                        if (cameraSelecionada > 0) {

                            cameraSelecionada--
                        }
                    },

                    onProximaClick = {

                        if (
                            cameraSelecionada + 1 <
                            cameras.size
                        ) {

                            cameraSelecionada++
                        }
                    }
                )
            }

            // FUNCIONÁRIOS

            "funcionarios" -> FuncionariosScreen(

                onBackClick = {

                    tela = "home"
                },

                funcionarios = funcionarios,

                cargos = cargos
            )

            // OCORRÊNCIAS

            "ocorrencias" -> OcorrenciasScreen(

                onBackClick = {

                    tela = "home"
                },

                onRegistrarClick = {

                    erroCadastro = null

                    tela = "registrar"
                },

                onDetalhesClick = { ocorrencia ->

                    ocorrenciaSelecionada = ocorrencia

                    tela = "detalhesOcorrencia"
                },

                ocorrencias = detalhesOcorrencias
            )

            // DETALHES DA OCORRÊNCIA
            // EXPORTAÇÃO DE PDF E CLOUDINARY

            "detalhesOcorrencia" -> ExportarPdfScreen(

                ocorrencia = detalhesOcorrencias.firstOrNull {

                    it.id == ocorrenciaSelecionada?.id

                } ?: ocorrenciaSelecionada,

                onBackClick = {

                    tela = "ocorrencias"
                },

                onAdicionarFuncionario = {

                    mensagemErro =
                        "A operação de vincular funcionário ainda não possui uma rota confirmada na API."
                },

                onAlterarStatus = {
                    val id = ocorrenciaSelecionada?.id
                    if (id == null) {
                        mensagemErro = "Nenhuma ocorrência selecionada."
                    } else if (!carregando) {
                        carregando = true
                        mensagemErro = null
                        scope.launch {
                            try {
                                repo.alterarStatusOcorrencia(id)
                                recarregar++
                            } catch (e: CancellationException) {
                                throw e
                            } catch (e: Exception) {
                                mensagemErro = e.message ?: "Falha ao alterar o status da ocorrência."
                            } finally {
                                carregando = false
                            }
                        }
                    }
                }
            )

            // CADASTRO DE OCORRÊNCIA

            "registrar" -> RegistrarOcorrenciaScreen(

                onBackClick = {

                    tela = "ocorrencias"
                },

                funcionarios = funcionarios,

                setores = setores,

                cameras = cameras,

                motivos = motivos,

                epis = epis,

                carregando = carregando,

                erroApi = null,

                onRegistrar = { dados ->

                    scope.launch {

                        erroCadastro = null

                        carregando = true

                        try {

                            val imagemBase64 = dados.evidenciaUri?.let {
                                EvidenciaLocal.lerBase64(contexto, it)
                            }
                            val dataHoraEvidencia = if (imagemBase64 != null) {
                                EvidenciaLocal.dataHoraSql(dados.data, dados.horario)
                            } else null
                            val idCriado: Int = repo.cadastrarOcorrencia(

                                dados.idSetor,

                                dados.idMotivo,

                                dados.descricao,

                                dados.idFuncionario,

                                dados.idCamera,

                                dados.idEpi,

                                dados.prioridade,

                                usuarioAtual?.id
                                    ?: error("Sessão inválida."),

                                imagemBase64,
                                dataHoraEvidencia
                            )

                            val instanteCadastro = Instant.now()
                            datasCadastro = datasCadastro.toMutableMap().apply {
                                put(idCriado, instanteCadastro)
                            }
                            datasLocais.edit().putString(
                                idCriado.toString(), instanteCadastro.toString()
                            ).apply()

                            recarregar++

                            tela = "ocorrencias"

                        } catch (e: CancellationException) {

                            throw e

                        } catch (e: Exception) {

                            erroCadastro = e.message
                                ?: "Não foi possível cadastrar a ocorrência."

                        } finally {

                            carregando = false
                        }
                    }
                }
            )
        }

        // CARREGAMENTO

        if (
            carregando &&
            tela != "login" &&
            tela != "registrar"
        ) {

            CircularProgressIndicator(

                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 110.dp),

                color = CorLaranjaPopup
            )
        }

        // POP-UP DE ERROS

        if (!erroAtual.isNullOrBlank()) {

            PopupErroOroscorp(

                mensagem = erroAtual,

                onFechar = {

                    fecharPopupErro()
                }
            )
        }
    }
}

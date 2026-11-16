
package com.example.oroscorp_adm.view.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.oroscorp_adm.model.*
import com.example.oroscorp_adm.repository.OroscorpRepository
import com.example.oroscorp_adm.service.SessionAuth

import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {

    val repo = remember { OroscorpRepository() }
    val scope = rememberCoroutineScope()

    // NAVEGAÇÃO
    var tela by remember {
        mutableStateOf("login")
    }

    var recarregar by remember {
        mutableIntStateOf(0)
    }

    var carregando by remember {
        mutableStateOf(false)
    }

    // MENSAGENS
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

    // SELEÇÕES
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

    // CONTROLE DE CARREGAMENTO
    var setoresOk by remember {
        mutableStateOf(false)
    }

    var funcionariosOk by remember {
        mutableStateOf(false)
    }

    var camerasOk by remember {
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

    // CONSULTAS DA API
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

        val avisos = mutableListOf<String>()

        suspend fun <T> tentar(
            nome: String,
            consulta: suspend () -> T,
            aplicar: (T) -> Unit
        ) {
            try {
                val resultado: T = consulta()
                aplicar(resultado)
            } catch (e: Exception) {
                avisos.add(
                    "$nome: ${e.message ?: "Falha de conexão"}"
                )
            }
        }

        when (tela) {

            "setores", "detalhesSetor" -> {

                tentar("Setores", repo::setores) {
                    setores = it
                    setoresOk = true
                }

                tentar("Câmeras", repo::cameras) {
                    cameras = it
                    camerasOk = true
                }

                tentar("Funcionários", repo::funcionarios) {
                    funcionarios = it
                    funcionariosOk = true
                }

                tentar("Cargos", repo::cargos) {
                    cargos = it
                }

                tentar("Ocorrências", repo::ocorrencias) {
                    ocorrencias = it
                    ocorrenciasOk = true
                }

                tentar(
                    "Status ocorrência",
                    repo::statusOcorrencias
                ) {
                    statusOcorrencias = it
                    statusOcorrenciasOk = true
                }

                tentar("Status câmera", repo::statusCameras) {
                    statusCameras = it
                    statusCamerasOk = true
                }

                tentar(
                    "Status funcionário",
                    repo::statusFuncionarios
                ) {
                    statusFuncionarios = it
                }
            }

            "cameras", "camera" -> {

                tentar("Câmeras", repo::cameras) {
                    cameras = it
                    camerasOk = true
                }

                tentar("Status câmera", repo::statusCameras) {
                    statusCameras = it
                    statusCamerasOk = true
                }
            }

            "funcionarios" -> {

                tentar("Funcionários", repo::funcionarios) {
                    funcionarios = it
                    funcionariosOk = true
                }

                tentar("Cargos", repo::cargos) {
                    cargos = it
                }
            }

            "home" -> {

                tentar("Câmeras", repo::cameras) {
                    cameras = it
                    camerasOk = true
                }

                tentar("Status câmera", repo::statusCameras) {
                    statusCameras = it
                    statusCamerasOk = true
                }

                tentar("Ocorrências", repo::ocorrencias) {
                    ocorrencias = it
                    ocorrenciasOk = true
                }

                tentar(
                    "Status ocorrência",
                    repo::statusOcorrencias
                ) {
                    statusOcorrencias = it
                    statusOcorrenciasOk = true
                }
            }

            "ocorrencias", "detalhesOcorrencia" -> {

                tentar("Ocorrências", repo::ocorrencias) {
                    ocorrencias = it
                    ocorrenciasOk = true
                }

                tentar(
                    "Status ocorrência",
                    repo::statusOcorrencias
                ) {
                    statusOcorrencias = it
                    statusOcorrenciasOk = true
                }

                tentar("Motivos", repo::motivos) {
                    motivos = it
                }

                tentar("Setores", repo::setores) {
                    setores = it
                    setoresOk = true
                }
            }

            "registrar" -> {

                tentar("Funcionários", repo::funcionarios) {
                    funcionarios = it
                    funcionariosOk = true
                }

                tentar("Setores", repo::setores) {
                    setores = it
                    setoresOk = true
                }

                tentar("Câmeras", repo::cameras) {
                    cameras = it
                    camerasOk = true
                }

                tentar("Motivos", repo::motivos) {
                    motivos = it
                }

                tentar("EPIs", repo::epis) {
                    epis = it
                }
            }
        }

        mensagemErro = avisos
            .takeIf { it.isNotEmpty() }
            ?.joinToString("\n")

        carregando = false
    }

    // INFORMAÇÕES DOS SETORES
    val setorInfo = setores.map { setor ->

        val doSetor = ocorrencias.filter {
            it.id_setor == setor.id
        }

        val temStatusIndefinido = doSetor.any { ocorrencia ->
            statusOcorrencias.none {
                it.id == ocorrencia.id_status_ocorrencia
            }
        }

        val abertos: Int? =
            if (
                ocorrenciasOk &&
                statusOcorrenciasOk &&
                !temStatusIndefinido
            ) {
                doSetor.count { ocorrencia ->

                    val nome = statusOcorrencias
                        .firstOrNull {
                            it.id == ocorrencia.id_status_ocorrencia
                        }
                        ?.status
                        .orEmpty()

                    nome.equals("Aberta", true) ||
                            nome.equals("Em aberto", true)
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
            } else null,
            cameras = if (camerasOk) {
                cameras.count {
                    it.id_setor == setor.id
                }
            } else null,
            abertas = abertos
        )
    }

    // DETALHES DAS OCORRÊNCIAS
    val detalhesOcorrencias = ocorrencias.map { item ->

        DetalhesOcorrencia(
            id = item.id,
            tipoIA = false,
            origemConhecida = false,
            status = statusOcorrencias
                .firstOrNull {
                    it.id == item.id_status_ocorrencia
                }
                ?.status
                .orEmpty(),
            prioridade = "—",
            tipo = motivos
                .firstOrNull {
                    it.id == item.id_motivo_ocorrencia
                }
                ?.nome
                .orEmpty(),
            setor = setores
                .firstOrNull {
                    it.id == item.id_setor
                }
                ?.nome
                .orEmpty()
        )
    }

    // CÂMERAS POR SETOR
    val cameraSetor = cameras.map { cam ->

        val status = statusCameras.firstOrNull {
            it.id == cam.id_status_camera
        }

        CameraInfo(
            id = cam.id,
            nome = cam.nome,
            setorId = cam.id_setor,
            online = status?.status.equals("online", true),
            statusConhecido = status != null
        )
    }

    // FUNCIONÁRIOS POR SETOR
    val funcionarioSetor = funcionarios.map { f ->

        val status = statusFuncionarios.firstOrNull {
            it.id == f.id_status_funcionario
        }

        FuncionarioInfo(
            nome = f.nome,
            cargo = cargos.firstOrNull {
                it.id == f.id_cargo
            }?.cargo ?: "—",
            ativo = status?.status.equals("ativo", true),
            setorId = f.id_setor,
            statusConhecido = status != null
        )
    }

    // OCORRÊNCIAS POR SETOR
    val ocorrenciaSetor = ocorrencias.map { o ->

        OcorrenciaInfo(
            codigo = "#${o.id}",
            funcionario = "—",
            data = "—",
            status = statusOcorrencias.firstOrNull {
                it.id == o.id_status_ocorrencia
            }?.status ?: "—",
            origem = "—",
            setorId = o.id_setor
        )
    }

    val online = cameras.count { cam ->
        statusCameras.firstOrNull {
            it.id == cam.id_status_camera
        }?.status.equals("online", true)
    }

    // INTERFACE E NAVEGAÇÃO
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF353535))
    ) {

        when (tela) {

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
                            usuarioAtual = repo.login(email, senha)
                            tela = "home"
                        } catch (e: Exception) {
                            erroLogin = e.message ?: "Falha no login"
                        } finally {
                            carregando = false
                        }
                    }
                },
                carregando = carregando,
                erro = erroLogin
            )

            "esqueceu" -> EsqueceuSenhaScreen(
                onBackClick = {
                    tela = "login"
                },
                onContinueClick = {},
                onEnviarEmail = {
                    erroSenha =
                        "Rota de recuperação de senha não confirmada."
                },
                erro = erroSenha
            )

            "codigo" -> RedefinirSenhaCodigo(
                onBackClick = {
                    tela = "esqueceu"
                },
                onContinueClick = {},
                onValidarCodigo = {
                    erroSenha =
                        "Rota de validação não confirmada."
                },
                erro = erroSenha
            )

            "trocar" -> TrocarSenhaScreen(
                onBackClick = {
                    tela = "codigo"
                },
                onRedefinirClick = {},
                onConfirmarSenha = {
                    erroSenha =
                        "Rota de alteração de senha não confirmada."
                },
                erro = erroSenha
            )

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
                onSairClick = {
                    tela = "login"
                    erroLogin = null
                    usuarioAtual = null
                    SessionAuth.clear()
                },
                onPerfilClick = {
                    tela = "perfil"
                },
                ocorrenciasHoje = "--",
                aguardandoIdentificacao = "--",
                camerasOnline =
                    if (camerasOk && statusCamerasOk) {
                        "$online/${cameras.size}"
                    } else {
                        "--"
                    },
                indiceSeguranca = "--"
            )

            "perfil" -> PerfilAdminScreen(
                usuario = usuarioAtual,
                onBackClick = {
                    tela = "home"
                }
            )

            "setores" -> SetoresScreen(
                onVoltar = {
                    tela = "home"
                },
                onAbrirSetor = {
                    setorSelecionado = it
                    tela = "detalhesSetor"
                },
                setores = setorInfo
            )

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

            "cameras" -> CamerasScreen(
                onBackClick = {
                    tela = "home"
                },
                cameras = cameras,
                statusCameras = statusCameras,
                onCameraClick = {
                    cameraSelecionada = it
                    tela = "camera"
                }
            )

            "camera" -> {

                val cam = cameras.getOrNull(cameraSelecionada)

                val status = cam?.let { camera ->
                    statusCameras.firstOrNull {
                        it.id == camera.id_status_camera
                    }
                }

                CameraScreen(
                    nomeCamera = cam?.nome ?: "Câmera indisponível",
                    videoUrl = cam?.endereco_stream,
                    online = status?.status.equals("online", true),
                    statusConhecido = status != null,
                    onBackClick = {
                        tela = "cameras"
                    },
                    temAnterior = cameraSelecionada > 0,
                    temProxima =
                        cameraSelecionada + 1 < cameras.size,
                    onAnteriorClick = {
                        if (cameraSelecionada > 0) {
                            cameraSelecionada--
                        }
                    },
                    onProximaClick = {
                        if (cameraSelecionada + 1 < cameras.size) {
                            cameraSelecionada++
                        }
                    }
                )
            }

            "funcionarios" -> FuncionariosScreen(
                onBackClick = {
                    tela = "home"
                },
                funcionarios = funcionarios,
                cargos = cargos
            )

            "ocorrencias" -> OcorrenciasScreen(
                onBackClick = {
                    tela = "home"
                },
                onRegistrarClick = {
                    erroCadastro = null
                    tela = "registrar"
                },
                onDetalhesClick = {
                    ocorrenciaSelecionada = it
                    tela = "detalhesOcorrencia"
                },
                ocorrencias = detalhesOcorrencias
            )

            "detalhesOcorrencia" -> DetalhesOcorrenciaScreen(
                ocorrencia = ocorrenciaSelecionada,
                onBackClick = {
                    tela = "ocorrencias"
                },
                onAdicionarFuncionario = {
                    mensagemErro =
                        "Rota de vínculo de funcionário não confirmada."
                },
                onAlterarStatus = {
                    mensagemErro =
                        "Rota de atualização de status não confirmada."
                },
                onExportarPDF = {
                    mensagemErro =
                        "Exportação de PDF não configurada."
                }
            )

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
                erroApi = erroCadastro,
                onRegistrar = { dados ->

                    scope.launch {
                        erroCadastro = null
                        carregando = true

                        try {
                            repo.cadastrarOcorrencia(
                                dados.idSetor,
                                dados.idMotivo,
                                dados.descricao,
                                dados.idFuncionario,
                                dados.idCamera,
                                dados.idEpi,
                                dados.prioridade
                            )

                            recarregar++
                            tela = "ocorrencias"

                        } catch (e: Exception) {
                            erroCadastro =
                                e.message ?: "Falha no cadastro"
                        } finally {
                            carregando = false
                        }
                    }
                }
            )
        }

        // INDICADOR DE CARREGAMENTO
        if (
            carregando &&
            tela != "login" &&
            tela != "registrar"
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 110.dp),
                color = Color(0xFFFF8800)
            )
        }

        // MENSAGENS DE ERRO
        if (
            mensagemErro != null &&
            tela != "login"
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        Color(0xFF1B1B1B),
                        RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp
                        )
                    )
                    .border(
                        1.dp,
                        Color(0xFFFF8800)
                    )
                    .padding(12.dp)
            ) {

                Text(
                    text = mensagemErro.orEmpty(),
                    color = Color(0xFFFFD8C0),
                    fontSize = 12.sp,
                    maxLines = 4
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    TextButton(
                        onClick = {
                            recarregar++
                            mensagemErro = null
                        }
                    ) {
                        Text("Tentar novamente")
                    }

                    TextButton(
                        onClick = {
                            mensagemErro = null
                        }
                    ) {
                        Text("Fechar")
                    }
                }
            }
        }
    }
}

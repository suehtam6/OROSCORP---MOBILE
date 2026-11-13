
package com.example.oroscorp_adm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.oroscorp_adm.model.Camera
import com.example.oroscorp_adm.model.StatusCamera
import com.example.oroscorp_adm.service.RetrofitFactory
import com.example.oroscorp_adm.ui.theme.OroscorpadmTheme
import com.example.oroscorp_adm.view.screens.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            OroscorpadmTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {

    var telaAtual by remember {
        mutableStateOf("login")
    }

    var cameraSelecionada by remember {
        mutableIntStateOf(0)
    }

    // Setor selecionado para visualizar os detalhes
    var setorSelecionado by remember {
        mutableIntStateOf(0)
    }

    // Lista de câmeras recebidas futuramente da API
    val cameras = remember {
        mutableStateListOf<Camera>()
    }

    // Ocorrência selecionada para visualização
    var ocorrenciaSelecionada by remember {
        mutableStateOf<DetalhesOcorrencia?>(null)
    }

    when (telaAtual) {

        // LOGIN
        "login" -> {
            LoginScreen(
                onEntrarClick = {
                    telaAtual = "home"
                },
                onEsqueceuSenhaClick = {
                    telaAtual = "esqueceu"
                }
            )
        }

        // DASHBOARD
        "home" -> {
            HomeScreen(
                onDashboardClick = {
                    telaAtual = "home"
                },
                onSetoresClick = {
                    telaAtual = "setores"
                    val call = RetrofitFactory().getSetorService().getSetores()
                },
                onCamerasClick = {
                    telaAtual = "cameras"
                },
                onOcorrenciasClick = {
                    telaAtual = "ocorrencias"
                },
                onFuncionariosClick = {
                    telaAtual = "funcionarios"
                },
                onSairClick = {
                    telaAtual = "login"
                }
            )
        }

        // FUNCIONÁRIOS
        "funcionarios" -> {
            FuncionariosScreen(
                onBackClick = {
                    telaAtual = "home"
                }
            )
        }

        // SETORES
        "setores" -> {
            SetoresScreen(
                onVoltar = {
                    telaAtual = "home"
                },
                onAbrirSetor = { id ->
                    setorSelecionado = id
                    telaAtual = "detalhes_setor"
                }
            )
        }

        // DETALHES DO SETOR
        "detalhes_setor" -> {
            DetalhesSetorScreen(
                setorId = setorSelecionado,
                onVoltar = {
                    telaAtual = "setores"
                }
            )
        }

        // LISTA DE CÂMERAS
        "cameras" -> {
            CamerasScreen(
                cameras = cameras,

                onBackClick = {
                    telaAtual = "home"
                },

                onCameraClick = { index ->
                    if (index in cameras.indices) {
                        cameraSelecionada = index
                        telaAtual = "camera"
                    }
                },

                statusCamera = StatusCamera(
                    idStatusCamera = 0,
                    status = ""
                ),

                statusCameras = emptyList()
            )
        }

        // VISUALIZAÇÃO DA CÂMERA
        "camera" -> {
            val camera = cameras.getOrNull(cameraSelecionada)

            if (camera != null) {
                CameraScreen(
                    nomeCamera = camera.nome,
                    videoUrl = camera.enderecoStream,

                    onBackClick = {
                        telaAtual = "cameras"
                    },

                    temAnterior = cameraSelecionada > 0,
                    temProxima = cameraSelecionada < cameras.lastIndex,

                    onAnteriorClick = {
                        if (cameraSelecionada > 0) {
                            cameraSelecionada--
                        }
                    },

                    onProximaClick = {
                        if (cameraSelecionada < cameras.lastIndex) {
                            cameraSelecionada++
                        }
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    telaAtual = "cameras"
                }
            }
        }

        // LISTA DE OCORRÊNCIAS
        "ocorrencias" -> {
            OcorrenciasScreen(
                onBackClick = {
                    telaAtual = "home"
                },

                onRegistrarClick = {
                    telaAtual = "registrarOcorrencia"
                },

                onDetalhesClick = { ocorrencia ->
                    ocorrenciaSelecionada = ocorrencia
                    telaAtual = "detalhesOcorrencia"
                }
            )
        }

        // REGISTRAR OCORRÊNCIA
        "registrarOcorrencia" -> {
            RegistrarOcorrenciaScreen(
                onBackClick = {
                    telaAtual = "ocorrencias"
                },

                onRegistrarSucesso = {
                    telaAtual = "ocorrencias"
                }
            )
        }

        // DETALHES DA OCORRÊNCIA
        "detalhesOcorrencia" -> {
            DetalhesOcorrenciaScreen(
                ocorrencia = ocorrenciaSelecionada,

                onBackClick = {
                    telaAtual = "ocorrencias"
                },

                onAdicionarFuncionario = {
                    // Futuramente selecionar funcionário pela API
                },

                onAlterarStatus = {
                    // Futuramente atualizar status pela API
                },

                onExportarPDF = {
                    // Futuramente gerar relatório PDF
                }
            )
        }

        // ESQUECEU SENHA
        "esqueceu" -> {
            EsqueceuSenhaScreen(
                onBackClick = {
                    telaAtual = "login"
                },

                onContinueClick = {
                    telaAtual = "codigo"
                }
            )
        }

        // CÓDIGO DE RECUPERAÇÃO
        "codigo" -> {
            RedefinirSenhaCodigo(
                onBackClick = {
                    telaAtual = "esqueceu"
                },

                onContinueClick = {
                    telaAtual = "trocar"
                }
            )
        }

        // TROCAR SENHA
        "trocar" -> {
            TrocarSenhaScreen(
                onBackClick = {
                    telaAtual = "codigo"
                },

                onRedefinirClick = {
                    telaAtual = "login"
                }
            )
        }
    }
}


package com.example.oroscorp_adm.view.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oroscorp_adm.R

private val FundoDetalhes = Color(0xFF353535)
private val CartaoDetalhes = Color(0xFF131619)
private val LaranjaDetalhes = Color(0xFFFF6600)
private val TextoSecundario = Color(0xFF9CA3AF)
private val BordaDetalhes = Color(0xFF272C32)

data class HistoricoOcorrencia(
    val horario: String,
    val descricao: String
)

data class DetalhesOcorrencia(
    val id: Int,
    val tipoIA: Boolean,
    val origemConhecida: Boolean = true,
    val status: String,
    val prioridade: String,
    val funcionario: String? = null,
    val matricula: String? = null,
    val cargo: String? = null,
    val tipo: String = "",
    val epi: String = "",
    val setor: String = "",
    val camera: String = "",
    val data: String = "",
    val horario: String = "",
    val descricao: String = "",
    val confiancaIA: Float? = null,
    val deteccaoIA: String? = null,
    val evidencia: Int? = null,
    val historico: List<HistoricoOcorrencia> = emptyList()
)

@Composable
fun DetalhesOcorrenciaScreen(
    ocorrencia: DetalhesOcorrencia?,
    onBackClick: () -> Unit,
    onAdicionarFuncionario: () -> Unit = {},
    onAlterarStatus: () -> Unit = {},
    onExportarPDF: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoDetalhes)
    ) {
        // CABEÇALHO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(95.dp)
                .background(Color.Black)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo OROSCORP",
                modifier = Modifier
                    .weight(1f)
                    .height(75.dp),
                contentScale = ContentScale.Fit,
                alignment = Alignment.CenterStart
            )

            Spacer(Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(55.dp)
                    .border(
                        2.5.dp,
                        LaranjaDetalhes,
                        RoundedCornerShape(18.dp)
                    )
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "←",
                    color = LaranjaDetalhes,
                    fontSize = 34.sp
                )
            }
        }

        if (ocorrencia == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Nenhuma ocorrência selecionada",
                    color = Color.White
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp)
            ) {
                Spacer(Modifier.height(12.dp))

                Text(
                    "Detalhes da ocorrência",
                    color = Color.White,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Text(
                    "#${ocorrencia.id.toString().padStart(5, '0')}",
                    color = LaranjaDetalhes,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(12.dp))

                // STATUS
                BlocoDetalhes {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        EtiquetaDetalhes(
                            ocorrencia.status.uppercase(),
                            LaranjaDetalhes
                        )

                        Spacer(Modifier.width(6.dp))

                        EtiquetaDetalhes(
                            if (!ocorrencia.origemConhecida) "NÃO INFORMADA" else if (ocorrencia.tipoIA) "IA" else "MANUAL",
                            TextoSecundario
                        )

                        Spacer(Modifier.width(6.dp))

                        EtiquetaDetalhes(
                            ocorrencia.prioridade,
                            Color(0xFFFFC107)
                        )
                    }

                    if (ocorrencia.origemConhecida && ocorrencia.tipoIA) {
                        Spacer(Modifier.height(12.dp))

                        Text(
                            "✧ Detectada automaticamente pela IA",
                            color = LaranjaDetalhes,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                // FUNCIONÁRIO
                BlocoDetalhes {
                    TituloBloco(
                        if (ocorrencia.funcionario == null)
                            "FUNCIONÁRIO NÃO IDENTIFICADO"
                        else
                            "FUNCIONÁRIO"
                    )

                    Spacer(Modifier.height(14.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(
                                    Color(0xFF302019),
                                    CircleShape
                                )
                                .border(
                                    1.dp,
                                    LaranjaDetalhes,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                ocorrencia.funcionario
                                    ?.split(" ")
                                    ?.mapNotNull { it.firstOrNull() }
                                    ?.take(2)
                                    ?.joinToString("")
                                    ?: "?",
                                color = LaranjaDetalhes,
                                fontSize = 15.sp
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Column {
                            Text(
                                ocorrencia.funcionario
                                    ?: "Não identificado",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )

                            Text(
                                if (ocorrencia.funcionario == null)
                                    if (ocorrencia.origemConhecida && ocorrencia.tipoIA)
                                        "Detecção automática pela IA" else "Funcionário não informado"
                                else
                                    "${ocorrencia.cargo.orEmpty()} - Mat. ${ocorrencia.matricula.orEmpty()}",
                                color = TextoSecundario,
                                fontSize = 11.sp
                            )
                        }
                    }

                    if (ocorrencia.origemConhecida && ocorrencia.tipoIA &&
                        ocorrencia.funcionario == null
                    ) {
                        Spacer(Modifier.height(15.dp))

                        Text(
                            "A inteligência artificial detectou uma possível irregularidade, mas não foi possível identificar automaticamente o funcionário.",
                            color = TextoSecundario,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(Modifier.height(14.dp))

                        Button(
                            onClick = onAdicionarFuncionario,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LaranjaDetalhes
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Adicionar funcionário")
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                // DETECÇÃO DA IA
                if (ocorrencia.origemConhecida && ocorrencia.tipoIA) {
                    BlocoDetalhes {
                        TituloBloco("DETECÇÃO DA IA")

                        Spacer(Modifier.height(10.dp))

                        Text(
                            "Confiança da análise",
                            color = TextoSecundario,
                            fontSize = 12.sp
                        )

                        val confianca = (
                                ocorrencia.confiancaIA ?: 0f
                                ).coerceIn(0f, 1f)

                        Text(
                            "${(confianca * 100).toInt()}%",
                            color = LaranjaDetalhes,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { confianca },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(7.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            color = LaranjaDetalhes,
                            trackColor = Color(0xFF292D33)
                        )

                        Spacer(Modifier.height(14.dp))

                        Text(
                            "Detecção: ${ocorrencia.deteccaoIA ?: "Não informada"}",
                            color = Color.LightGray,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(Modifier.height(10.dp))
                }

                // INFORMAÇÕES
                BlocoDetalhes {
                    TituloBloco("INFORMAÇÕES")

                    Spacer(Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(Modifier.weight(1f)) {
                            CampoDetalhes("Tipo", ocorrencia.tipo)
                        }
                        Box(Modifier.weight(1f)) {
                            CampoDetalhes("EPI", ocorrencia.epi)
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(Modifier.weight(1f)) {
                            CampoDetalhes("Setor", ocorrencia.setor)
                        }
                        Box(Modifier.weight(1f)) {
                            CampoDetalhes("Câmera", ocorrencia.camera)
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(Modifier.weight(1f)) {
                            CampoDetalhes("Data", ocorrencia.data)
                        }
                        Box(Modifier.weight(1f)) {
                            CampoDetalhes("Horário", ocorrencia.horario)
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                // DESCRIÇÃO
                BlocoDetalhes {
                    TituloBloco("DESCRIÇÃO")

                    Spacer(Modifier.height(12.dp))

                    Text(
                        ocorrencia.descricao.ifBlank {
                            "Descrição não informada."
                        },
                        color = Color(0xFFCDD0D5),
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }

                Spacer(Modifier.height(10.dp))

                // EVIDÊNCIA
                BlocoDetalhes {
                    Text(
                        if (ocorrencia.origemConhecida && ocorrencia.tipoIA)
                            "FRAME CAPTURADO PELA CÂMERA"
                        else
                            "EVIDÊNCIA",
                        color = LaranjaDetalhes,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Black)
                            .border(
                                1.dp,
                                BordaDetalhes,
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (ocorrencia.evidencia != null) {
                            Image(
                                painter = painterResource(
                                    ocorrencia.evidencia
                                ),
                                contentDescription = "Evidência",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Text(
                                "Nenhuma evidência disponível",
                                color = TextoSecundario,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                // HISTÓRICO
                BlocoDetalhes {
                    TituloBloco("HISTÓRICO")

                    Spacer(Modifier.height(14.dp))

                    if (ocorrencia.historico.isEmpty()) {
                        Text(
                            "Nenhum registro no histórico.",
                            color = TextoSecundario,
                            fontSize = 12.sp
                        )
                    } else {
                        ocorrencia.historico.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 9.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 5.dp)
                                        .size(9.dp)
                                        .background(
                                            LaranjaDetalhes,
                                            CircleShape
                                        )
                                )

                                Spacer(Modifier.width(12.dp))

                                Column {
                                    Text(
                                        item.horario,
                                        color = TextoSecundario,
                                        fontSize = 11.sp
                                    )

                                    Text(
                                        item.descricao,
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // AÇÕES
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val fechada = ocorrencia.status.equals(
                        "Fechada",
                        ignoreCase = true
                    )

                    OutlinedButton(
                        onClick = onAlterarStatus,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(
                            1.dp,
                            if (fechada)
                                Color(0xFF00B981)
                            else
                                Color(0xFFFF5555)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            if (fechada)
                                "Reabrir ocorrência"
                            else
                                "Fechar ocorrência",
                            color = if (fechada)
                                Color(0xFF00B981)
                            else
                                Color(0xFFFF5555),
                            fontSize = 11.sp
                        )
                    }

                    OutlinedButton(
                        onClick = onExportarPDF,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(
                            1.dp,
                            LaranjaDetalhes
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "Exportar PDF",
                            color = LaranjaDetalhes,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun BlocoDetalhes(
    conteudo: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CartaoDetalhes)
            .border(
                1.dp,
                BordaDetalhes,
                RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        content = conteudo
    )
}

@Composable
private fun TituloBloco(titulo: String) {
    Text(
        titulo,
        color = LaranjaDetalhes,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun EtiquetaDetalhes(
    texto: String,
    cor: Color
) {
    Box(
        modifier = Modifier
            .border(
                1.dp,
                cor.copy(alpha = 0.5f),
                RoundedCornerShape(20.dp)
            )
            .background(
                cor.copy(alpha = 0.08f),
                RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 9.dp, vertical = 6.dp)
    ) {
        Text(
            texto,
            color = cor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CampoDetalhes(
    titulo: String,
    valor: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 55.dp)
            .background(
                Color(0xFF191D22),
                RoundedCornerShape(8.dp)
            )
            .border(
                1.dp,
                BordaDetalhes,
                RoundedCornerShape(8.dp)
            )
            .padding(10.dp)
    ) {
        Text(
            titulo,
            color = TextoSecundario,
            fontSize = 10.sp
        )

        Spacer(Modifier.height(4.dp))

        Text(
            valor.ifBlank { "Não informado" },
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

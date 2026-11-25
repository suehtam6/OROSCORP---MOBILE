
package com.example.oroscorp_adm.view.screens

import com.example.oroscorp_adm.ui.theme.TemaGlobal
import com.example.oroscorp_adm.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// CORES
private val cartaoSetor
    get() = TemaGlobal.cor(Color(0xFF14171C), Color(0xFFFFFFFF))
private val internoSetor
    get() = TemaGlobal.cor(Color(0xFF1B1F25), Color(0xFFE9EBEE))
private val laranjaSetor
    get() = TemaGlobal.cor(Color(0xFFFF7900), Color(0xFFB85A00))
private val verdeSetor
    get() = TemaGlobal.cor(Color(0xFF21CA70), Color(0xFF00864A))
private val vermelhoSetor
    get() = TemaGlobal.cor(Color(0xFFFF454D), Color(0xFFB3261E))
private val cinzaSetor
    get() = TemaGlobal.cor(Color(0xFF9BA6B7), Color(0xFF60656B))

// MODELOS DOS DADOS

data class SetorInfo(
    val id: Int,
    val nome: String,
    val descricao: String,
    val funcionarios: Int?,
    val cameras: Int?,
    val abertas: Int?
)

data class CameraInfo(
    val id: Int,
    val nome: String,
    val setorId: Int,
    val online: Boolean,
    val statusConhecido: Boolean = true
)

data class FuncionarioInfo(
    val nome: String,
    val cargo: String,
    val ativo: Boolean,
    val setorId: Int,
    val statusConhecido: Boolean = true
)

data class OcorrenciaInfo(
    val codigo: String,
    val funcionario: String,
    val data: String,
    val status: String,
    val origem: String,
    val setorId: Int
)

// CABEÇALHO COM LOGO ORIGINAL

@Composable
private fun CabecalhoSetor(
    onVoltar: () -> Unit
) {
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
                .height(75.dp).background(Color.Black, RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Fit,
            alignment = Alignment.CenterStart
        )

        Spacer(Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .size(55.dp)
                .border(
                    2.5.dp,
                    TemaGlobal.cor(colorResource(R.color.Laranja), Color(0xFFB85A00)),
                    RoundedCornerShape(18.dp)
                )
                .clickable { onVoltar() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "←",
                color = TemaGlobal.cor(colorResource(R.color.Laranja), Color(0xFFB85A00)),
                fontSize = 34.sp
            )
        }
    }
}

// INDICADORES

@Composable
private fun IndicadorSetor(
    titulo: String,
    numero: Int?,
    modifier: Modifier = Modifier,
    destaque: Boolean = false
) {
    Column(
        modifier = modifier
            .background(
                internoSetor,
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                TemaGlobal.cor(Color(0xFF282D35), Color(0xFF9CA3AF)),
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = numero?.toString() ?: "—",
            color = if (destaque) laranjaSetor else TemaGlobal.cor(Color.White, Color(0xFF191C1F)),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(5.dp))

        Text(
            text = titulo,
            color = cinzaSetor,
            fontSize = 11.sp,
            maxLines = 1
        )
    }
}

// CARTÃO DO SETOR

@Composable
private fun CartaoSetor(
    setor: SetorInfo,
    onAbrir: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = TemaGlobal.cor(colorResource(R.color.Cartao), Color(0xFFFFFFFF))
        ),
        shape = RoundedCornerShape(18.dp),
        border = if (TemaGlobal.modoClaro) androidx.compose.foundation.BorderStroke(1.dp, TemaGlobal.borda) else null
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = setor.nome,
                color = TemaGlobal.cor(Color.White, Color(0xFF191C1F)),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = setor.descricao,
                color = cinzaSetor,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )

            Spacer(Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IndicadorSetor(
                    titulo = "Funcionários",
                    numero = setor.funcionarios,
                    modifier = Modifier.weight(1f)
                )

                IndicadorSetor(
                    titulo = "Câmeras",
                    numero = setor.cameras,
                    modifier = Modifier.weight(1f)
                )

                IndicadorSetor(
                    titulo = "Abertas",
                    numero = setor.abertas,
                    modifier = Modifier.weight(1f),
                    destaque = true
                )
            }

            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        internoSetor,
                        RoundedCornerShape(12.dp)
                    )
                    .border(
                        1.dp,
                        TemaGlobal.cor(Color(0xFF303640), Color(0xFF9CA3AF)),
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { onAbrir() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ver setor  ›",
                    color = TemaGlobal.cor(Color.White, Color(0xFF191C1F)),
                    fontSize = 14.sp
                )
            }
        }
    }
}

// TELA PRINCIPAL DE SETORES

@Composable
fun SetoresScreen(
    onVoltar: () -> Unit,
    onAbrirSetor: (Int) -> Unit,
    setores: List<SetorInfo> = emptyList()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TemaGlobal.cor(colorResource(R.color.Fundo), Color(0xFFF2F3F5)))
    ) {
        CabecalhoSetor(onVoltar)

        LazyColumn(
            contentPadding = PaddingValues(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Setores",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TemaGlobal.cor(Color.White, Color(0xFF191C1F))
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Áreas monitoradas da planta industrial.",
                    color = cinzaSetor,
                    fontSize = 14.sp
                )
            }

            if (setores.isEmpty()) {
                item {
                    MensagemSemDados(
                        "Nenhum setor disponível. Aguardando dados da API."
                    )
                }
            } else {
                items(
                    items = setores,
                    key = { it.id }
                ) { setor ->
                    CartaoSetor(
                        setor = setor,
                        onAbrir = { onAbrirSetor(setor.id) }
                    )
                }
            }
        }
    }
}

// MENSAGEM PARA LISTAS VAZIAS

@Composable
private fun MensagemSemDados(
    mensagem: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                cartaoSetor,
                RoundedCornerShape(15.dp)
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = mensagem,
            color = cinzaSetor,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}

// PAINEL DOS DETALHES

@Composable
private fun PainelSetor(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                TemaGlobal.cor(Color(0xFF171717), Color(0xFFFFFFFF)),
                RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        content = content
    )
}

@Composable
private fun TituloPainel(
    texto: String
) {
    Text(
        text = texto,
        color = laranjaSetor,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

// CÂMERAS DO SETOR

@Composable
private fun LinhaCameraSetor(
    camera: CameraInfo
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                TemaGlobal.cor(Color(0xFF0D1012), Color(0xFFFFFFFF)),
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                TemaGlobal.cor(Color(0xFF282D35), Color(0xFF9CA3AF)),
                RoundedCornerShape(12.dp)
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "▣",
            color = cinzaSetor,
            fontSize = 20.sp
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = camera.nome,
            color = TemaGlobal.cor(Color.White, Color(0xFF191C1F)),
            modifier = Modifier.weight(1f),
            fontSize = 14.sp
        )

        Text(
            text = if (!camera.statusConhecido) "● INDISPONÍVEL"
                else if (camera.online) "● ONLINE" else "● OFFLINE",
            color = if (!camera.statusConhecido) cinzaSetor
                else if (camera.online) verdeSetor else vermelhoSetor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// FUNCIONÁRIOS DO SETOR

@Composable
private fun LinhaFuncionario(
    funcionario: FuncionarioInfo
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                0.5.dp,
                TemaGlobal.cor(Color.Gray, Color(0xFF60656B))
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = funcionario.nome,
            color = TemaGlobal.cor(Color.White, Color(0xFF191C1F)),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1.2f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = funcionario.cargo,
            color = cinzaSetor,
            fontSize = 11.sp,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = if (!funcionario.statusConhecido) "● INDISPONÍVEL"
                else if (funcionario.ativo) "● ATIVO" else "● INATIVO",
            color = if (!funcionario.statusConhecido) cinzaSetor
                else if (funcionario.ativo) verdeSetor else vermelhoSetor,
            fontSize = 10.sp
        )
    }
}

// OCORRÊNCIAS DO SETOR

@Composable
private fun CartaoOcorrencia(
    ocorrencia: OcorrenciaInfo
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                TemaGlobal.cor(Color(0xFF0D1012), Color(0xFFFFFFFF)),
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                TemaGlobal.cor(Color(0xFF282D35), Color(0xFF9CA3AF)),
                RoundedCornerShape(12.dp)
            )
            .padding(14.dp)
    ) {
        Row {
            Text(
                text = ocorrencia.codigo,
                color = cinzaSetor,
                fontSize = 12.sp
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = ocorrencia.origem,
                color = laranjaSetor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "${ocorrencia.funcionario} · ${ocorrencia.data}",
            color = cinzaSetor,
            fontSize = 12.sp
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "● ${ocorrencia.status}",
            color = if (
                ocorrencia.status.equals(
                    "ABERTA",
                    ignoreCase = true
                )
            ) {
                laranjaSetor
            } else {
                verdeSetor
            },
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// TELA DE DETALHES DO SETOR

@Composable
fun DetalhesSetorScreen(
    setorId: Int,
    onVoltar: () -> Unit,
    setores: List<SetorInfo> = emptyList(),
    cameras: List<CameraInfo> = emptyList(),
    funcionarios: List<FuncionarioInfo> = emptyList(),
    ocorrencias: List<OcorrenciaInfo> = emptyList()
) {
    val setor = setores.find { it.id == setorId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TemaGlobal.cor(colorResource(R.color.Fundo), Color(0xFFF2F3F5)))
    ) {
        CabecalhoSetor(onVoltar)

        if (setor == null) {
            MensagemSemDados(
                "Dados do setor indisponíveis. Aguardando dados da API."
            )
        } else {
            val camerasDoSetor = cameras.filter {
                it.setorId == setorId
            }

            val funcionariosDoSetor = funcionarios.filter {
                it.setorId == setorId
            }

            val ocorrenciasDoSetor = ocorrencias.filter {
                it.setorId == setorId
            }

            LazyColumn(
                contentPadding = PaddingValues(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // NOME E DESCRIÇÃO

                item {
                    Text(
                        text = setor.nome,
                        color = TemaGlobal.cor(Color.White, Color(0xFF191C1F)),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = setor.descricao,
                        color = cinzaSetor,
                        fontSize = 16.sp,
                        lineHeight = 25.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // OCORRÊNCIAS ABERTAS

                item {
                    PainelSetor {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 15.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = setor.abertas?.toString() ?: "—",
                                color = laranjaSetor,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.width(22.dp))

                            Text(
                                text = "Ocorrências abertas",
                                color = cinzaSetor,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                // CÂMERAS

                item {
                    PainelSetor {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = setor.cameras?.toString() ?: "—",
                                color = TemaGlobal.cor(Color.White, Color(0xFF191C1F)),
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.width(18.dp))

                            Text(
                                text = "CÂMERAS DO SETOR",
                                color = laranjaSetor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        if (camerasDoSetor.isEmpty()) {
                            Text(
                                text = "Nenhuma câmera disponível.",
                                color = cinzaSetor
                            )
                        } else {
                            camerasDoSetor.forEach { camera ->
                                LinhaCameraSetor(camera)
                                Spacer(Modifier.height(10.dp))
                            }
                        }
                    }
                }

                // FUNCIONÁRIOS

                item {
                    PainelSetor {
                        TituloPainel("FUNCIONÁRIOS DO SETOR")

                        Spacer(Modifier.height(16.dp))

                        if (funcionariosDoSetor.isEmpty()) {
                            Text(
                                text = "Nenhum funcionário disponível.",
                                color = cinzaSetor
                            )
                        } else {
                            funcionariosDoSetor.forEach { funcionario ->
                                LinhaFuncionario(funcionario)
                            }
                        }
                    }
                }

                // OCORRÊNCIAS RECENTES

                item {
                    PainelSetor {
                        TituloPainel("OCORRÊNCIAS RECENTES")

                        Spacer(Modifier.height(18.dp))

                        if (ocorrenciasDoSetor.isEmpty()) {
                            Text(
                                text = "Nenhuma ocorrência disponível.",
                                color = cinzaSetor
                            )
                        } else {
                            ocorrenciasDoSetor.forEach { ocorrencia ->
                                CartaoOcorrencia(ocorrencia)
                                Spacer(Modifier.height(10.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

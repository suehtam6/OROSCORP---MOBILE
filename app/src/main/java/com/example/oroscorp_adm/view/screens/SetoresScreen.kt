
package com.example.oroscorp_adm.view.screens

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

private val fundoSetor = Color(0xFF343434)
private val cartaoSetor = Color(0xFF14171C)
private val internoSetor = Color(0xFF1B1F25)
private val laranjaSetor = Color(0xFFFF7900)
private val verdeSetor = Color(0xFF21CA70)
private val vermelhoSetor = Color(0xFFFF454D)
private val cinzaSetor = Color(0xFF9BA6B7)

// MODELOS DOS DADOS

data class SetorInfo(
    val id: Int,
    val nome: String,
    val descricao: String,
    val funcionarios: Int,
    val cameras: Int,
    val abertas: Int
)

data class CameraInfo(
    val id: Int,
    val nome: String,
    val setorId: Int,
    val online: Boolean
)

data class FuncionarioInfo(
    val nome: String,
    val cargo: String,
    val ativo: Boolean,
    val setorId: Int
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
                    colorResource(R.color.Laranja),
                    RoundedCornerShape(18.dp)
                )
                .clickable { onVoltar() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "←",
                color = colorResource(R.color.Laranja),
                fontSize = 34.sp
            )
        }
    }
}

// INDICADORES

@Composable
private fun IndicadorSetor(
    titulo: String,
    numero: Int,
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
                Color(0xFF282D35),
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = numero.toString(),
            color = if (destaque) laranjaSetor else Color.White,
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
            containerColor = cartaoSetor
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = setor.nome,
                color = Color.White,
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
                        Color(0xFF303640),
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { onAbrir() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ver setor  ›",
                    color = Color.White,
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
            .background(fundoSetor)
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
                    color = Color.White
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
                Color(0xFF171717),
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
                Color(0xFF0D1012),
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                Color(0xFF282D35),
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
            color = Color.White,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp
        )

        Text(
            text = if (camera.online) {
                "● ONLINE"
            } else {
                "● OFFLINE"
            },
            color = if (camera.online) {
                verdeSetor
            } else {
                vermelhoSetor
            },
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
                Color.Gray
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = funcionario.nome,
            color = Color.White,
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
            text = if (funcionario.ativo) {
                "● ATIVO"
            } else {
                "● INATIVO"
            },
            color = if (funcionario.ativo) {
                verdeSetor
            } else {
                vermelhoSetor
            },
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
                Color(0xFF0D1012),
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                Color(0xFF282D35),
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
            .background(fundoSetor)
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
                        color = Color.White,
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
                                text = setor.abertas.toString(),
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
                                text = setor.cameras.toString(),
                                color = Color.White,
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

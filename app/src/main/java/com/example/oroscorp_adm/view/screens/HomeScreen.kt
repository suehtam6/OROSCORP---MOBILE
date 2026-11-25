
package com.example.oroscorp_adm.view.screens

import com.example.oroscorp_adm.ui.theme.TemaGlobal
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oroscorp_adm.R

private val Fundo
    get() = TemaGlobal.cor(Color(0xFF1C1E20), Color(0xFFF2F3F5))
private val Cartao
    get() = TemaGlobal.cor(Color(0xFF101315), Color(0xFFFFFFFF))
private val CartaoSecundario
    get() = TemaGlobal.cor(Color(0xFF191C1F), Color(0xFFE9EBEE))
private val Laranja
    get() = TemaGlobal.cor(Color(0xFFFF9500), Color(0xFFB85A00))
private val Branco
    get() = TemaGlobal.cor(Color(0xFFF5F5F5), Color(0xFF191C1F))
private val Cinza
    get() = TemaGlobal.cor(Color(0xFF9CA0A6), Color(0xFF60656B))
private val Verde
    get() = TemaGlobal.cor(Color(0xFF20C982), Color(0xFF00864A))
private val Borda
    get() = TemaGlobal.cor(Color(0xFF292D31), Color(0xFF9CA3AF))

@Composable
fun HomeScreen(
    onDashboardClick: () -> Unit = {},
    onSetoresClick: () -> Unit = {},
    onCamerasClick: () -> Unit = {},
    onOcorrenciasClick: () -> Unit = {},
    onFuncionariosClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {},
    onSairClick: () -> Unit = {},
    ocorrenciasHoje: Int? = null,
    aguardandoIdentificacao: Int? = null,
    camerasOnline: Int? = null,
    totalCameras: Int? = null,
    nomeUsuario: String = "Administrador",
    carregando: Boolean = false,
    erro: String? = null,
    onAtualizarClick: () -> Unit = {},
    dadosGrafico: List<Int> = emptyList()
) {
    var menuAberto by remember { mutableStateOf(false) }
    var periodo by remember { mutableStateOf("Semana") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // CABEÇALHO
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(Color.Black)
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .border(1.dp, Laranja, CircleShape)
                        .clickable { menuAberto = !menuAberto },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "☰",
                        color = Laranja,
                        fontSize = 21.sp
                    )
                }

                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "OROSCORP",
                    modifier = Modifier
                        .width(165.dp)
                        .height(46.dp).background(Color.Black, RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Fit
                )

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clickable { onAtualizarClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "↻",
                        color = Laranja,
                        fontSize = 26.sp
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 18.dp,
                    end = 18.dp,
                    top = 24.dp,
                    bottom = 28.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        "Dashboard",
                        color = Branco,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        "Visão geral do monitoramento de segurança e conformidade com EPIs.",
                        color = Cinza,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(Modifier.height(15.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    Verde.copy(alpha = 0.13f),
                                    RoundedCornerShape(20.dp)
                                )
                                .border(
                                    1.dp,
                                    Verde.copy(alpha = 0.4f),
                                    RoundedCornerShape(20.dp)
                                )
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 7.dp
                                )
                        ) {
                            Text(
                                "● Sistema de monitoramento",
                                color = Verde,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                if (carregando) {
                    item {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = Laranja,
                            trackColor = Cartao
                        )
                    }
                }

                if (!erro.isNullOrBlank()) {
                    item {
                        Text(
                            text = erro,
                            color = TemaGlobal.cor(Color(0xFFFF8C80), Color(0xFFB3261E)),
                            fontSize = 12.sp
                        )
                    }
                }

                // INDICADORES
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IndicadorDashboard(
                            titulo = "Ocorrências hoje",
                            valor = ocorrenciasHoje?.toString() ?: "—",
                            icone = "⚠",
                            modifier = Modifier.weight(1f)
                        )

                        IndicadorDashboard(
                            titulo = "Aguardando identificação",
                            valor = aguardandoIdentificacao
                                ?.toString() ?: "—",
                            icone = "◎",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IndicadorDashboard(
                            titulo = "Câmeras online",
                            valor = if (
                                camerasOnline != null &&
                                totalCameras != null
                            ) {
                                "$camerasOnline/$totalCameras"
                            } else {
                                "—"
                            },
                            icone = "▣",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // GRÁFICO
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Cartao,
                                RoundedCornerShape(14.dp)
                            )
                            .border(
                                1.dp,
                                Borda,
                                RoundedCornerShape(14.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            "Ocorrências ao longo do tempo",
                            color = Branco,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(Modifier.height(5.dp))

                        Text(
                            "Detecções de IA e registros manuais",
                            color = Cinza,
                            fontSize = 11.sp
                        )

                        Spacer(Modifier.height(14.dp))

                        Row(
                            horizontalArrangement =
                                Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                "Hoje",
                                "Semana",
                                "Mês"
                            ).forEach { opcao ->
                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (periodo == opcao)
                                                Laranja.copy(
                                                    alpha = 0.15f
                                                )
                                            else
                                                CartaoSecundario,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                            1.dp,
                                            if (periodo == opcao)
                                                Laranja
                                            else
                                                Borda,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            periodo = opcao
                                        }
                                        .padding(
                                            horizontal = 13.dp,
                                            vertical = 8.dp
                                        )
                                ) {
                                    Text(
                                        opcao,
                                        color = if (
                                            periodo == opcao
                                        ) {
                                            Laranja
                                        } else {
                                            Cinza
                                        },
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(22.dp))

                        GraficoOcorrencias(
                            valores = dadosGrafico,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(170.dp)
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            if (dadosGrafico.isEmpty())
                                "Sem dados disponíveis para o gráfico."
                            else
                                "Dados de ocorrências recebidos do sistema.",
                            color = Cinza,
                            fontSize = 10.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // ALERTAS
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Cartao,
                                RoundedCornerShape(14.dp)
                            )
                            .border(
                                1.dp,
                                Borda,
                                RoundedCornerShape(14.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            "Alertas recentes",
                            color = Branco,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(Modifier.height(5.dp))

                        Text(
                            "Ocorrências pendentes de tratativa",
                            color = Cinza,
                            fontSize = 11.sp
                        )

                        Spacer(Modifier.height(25.dp))

                        Text(
                            "Consulte as ocorrências para visualizar os alertas registrados.",
                            color = Cinza,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(22.dp))

                        Button(
                            onClick = onOcorrenciasClick,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Laranja
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                "Gerenciar ocorrências",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // ACESSOS
                item {
                    Text(
                        "Acesso rápido",
                        color = Branco,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {
                        AtalhoDashboard(
                            titulo = "Setores",
                            icone = "▦",
                            modifier = Modifier.weight(1f),
                            onClick = onSetoresClick
                        )

                        AtalhoDashboard(
                            titulo = "Câmeras",
                            icone = "▣",
                            modifier = Modifier.weight(1f),
                            onClick = onCamerasClick
                        )
                    }
                }

                item {
                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {
                        AtalhoDashboard(
                            titulo = "Ocorrências",
                            icone = "⚠",
                            modifier = Modifier.weight(1f),
                            onClick = onOcorrenciasClick
                        )

                        AtalhoDashboard(
                            titulo = "Funcionários",
                            icone = "♙",
                            modifier = Modifier.weight(1f),
                            onClick = onFuncionariosClick
                        )
                    }
                }
            }
        }

        // MENU LATERAL
        if (menuAberto) {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(275.dp)
                        .background(TemaGlobal.cor(Color(0xFF141617), Color(0xFFFFFFFF)))
                        .border(1.dp, Borda)
                        .verticalScroll(rememberScrollState())
                        .padding(18.dp)
                ) {
                    Spacer(Modifier.height(20.dp))

                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "OROSCORP",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp).background(Color.Black, RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(Modifier.height(30.dp))

                    Text(
                        "CENTRAL",
                        color = Laranja,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(Modifier.height(25.dp))

                    ItemMenu("Dashboard", "▤") {
                        menuAberto = false
                        onDashboardClick()
                    }

                    ItemMenu("Setores", "▦") {
                        menuAberto = false
                        onSetoresClick()
                    }

                    ItemMenu("Câmeras", "▣") {
                        menuAberto = false
                        onCamerasClick()
                    }

                    ItemMenu("Ocorrências", "⚠") {
                        menuAberto = false
                        onOcorrenciasClick()
                    }

                    ItemMenu("Funcionários", "♙") {
                        menuAberto = false
                        onFuncionariosClick()
                    }

                    Spacer(Modifier.height(25.dp))

                    HorizontalDivider(color = Borda)

                    Spacer(Modifier.height(20.dp))

                    Text(
                        nomeUsuario,
                        color = Branco,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Text(
                        "ADMINISTRADOR",
                        color = Cinza,
                        fontSize = 11.sp
                    )

                    Spacer(Modifier.height(15.dp))

                    OutlinedButton(
                        onClick = {
                            menuAberto = false
                            onPerfilClick()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, Laranja)
                    ) {
                        Text(
                            "Meu perfil",
                            color = Laranja
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            menuAberto = false
                            onSairClick()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFF8E3838)
                        )
                    ) {
                        Text(
                            "Sair da conta",
                            color = TemaGlobal.cor(Color(0xFFFF7777), Color(0xFFB3261E))
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color.Black.copy(alpha = 0.6f)
                        )
                        .clickable {
                            menuAberto = false
                        }
                )
            }
        }
    }
}

@Composable
private fun IndicadorDashboard(
    titulo: String,
    valor: String,
    icone: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(145.dp)
            .background(
                Cartao,
                RoundedCornerShape(14.dp)
            )
            .border(
                1.dp,
                Borda,
                RoundedCornerShape(14.dp)
            )
            .padding(15.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    Laranja.copy(alpha = 0.12f),
                    RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                icone,
                color = Laranja,
                fontSize = 19.sp
            )
        }

        Text(
            valor,
            color = Branco,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            titulo,
            color = Cinza,
            fontSize = 11.sp,
            lineHeight = 14.sp
        )
    }
}

@Composable
private fun AtalhoDashboard(
    titulo: String,
    icone: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .height(105.dp)
            .background(
                Cartao,
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                Borda,
                RoundedCornerShape(12.dp)
            )
            .clickable {
                onClick()
            }
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            icone,
            color = Laranja,
            fontSize = 24.sp
        )

        Text(
            titulo,
            color = Branco,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ItemMenu(
    titulo: String,
    icone: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            icone,
            color = Laranja,
            fontSize = 20.sp
        )

        Spacer(Modifier.width(16.dp))

        Text(
            titulo,
            color = Branco,
            fontSize = 14.sp
        )
    }

    HorizontalDivider(color = Borda)
}

@Composable
private fun GraficoOcorrencias(
    valores: List<Int>,
    modifier: Modifier = Modifier
) {
    val valoresValidos = valores.map {
        it.coerceAtLeast(0)
    }

    Canvas(modifier = modifier) {
        val largura = size.width
        val altura = size.height

        val maximo = (
                valoresValidos.maxOrNull() ?: 0
                )
            .coerceAtLeast(1)
            .toFloat()

        for (i in 0..4) {
            val y = altura * i / 4f

            drawLine(
                color = Borda,
                start = androidx.compose.ui.geometry.Offset(
                    0f,
                    y
                ),
                end = androidx.compose.ui.geometry.Offset(
                    largura,
                    y
                ),
                strokeWidth = 1.dp.toPx()
            )
        }

        if (valoresValidos.size >= 2) {
            val caminho = Path()

            valoresValidos.forEachIndexed { index, valor ->
                val x = largura * index /
                        (valoresValidos.size - 1).toFloat()

                val y = altura -
                        (valor / maximo) * altura

                if (index == 0) {
                    caminho.moveTo(x, y)
                } else {
                    caminho.lineTo(x, y)
                }
            }

            drawPath(
                path = caminho,
                color = Laranja,
                style = Stroke(
                    width = 3.dp.toPx()
                )
            )
        }
    }
}

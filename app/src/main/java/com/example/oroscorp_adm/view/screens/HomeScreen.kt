package com.example.oroscorp_adm.view.screens
import com.example.oroscorp_adm.R
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex

@Composable
fun HomeScreen(
    onDashboardClick: () -> Unit = {},
    onSetoresClick: () -> Unit = {},
    onCamerasClick: () -> Unit = {},
    onOcorrenciasClick: () -> Unit = {},
    onFuncionariosClick: () -> Unit = {},
    onSairClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {},
    ocorrenciasHoje: String = "--",
    aguardandoIdentificacao: String = "--",
    camerasOnline: String = "--",
    indiceSeguranca: String = "--"
) {
    var menuAberto by remember { mutableStateOf(false) }
    var periodo by remember { mutableStateOf("Semana") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.Fundo))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // LOGO NO CABEÇALHO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(95.dp)
                    .background(Color.Black),
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo OROSCORP",
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .height(80.dp)
                        .padding(start = 12.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp)
            ) {
                Spacer(Modifier.height(8.dp))

                // BOTÃO HAMBÚRGUER
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .border(2.dp, colorResource(R.color.Laranja), CircleShape)
                        .clickable { menuAberto = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text("☰", color = colorResource(R.color.Laranja), fontSize = 28.sp)
                }

                Spacer(Modifier.height(22.dp))

                // INDICADORES DO DASHBOARD
                Indicador(
                    titulo = "Ocorrências hoje",
                    valor = ocorrenciasHoje,
                    simbolo = "⚠",
                    variacao = null
                )

                Spacer(Modifier.height(22.dp))

                Indicador(
                    titulo = "Aguardando identificação",
                    valor = aguardandoIdentificacao,
                    simbolo = "◎"
                )

                Spacer(Modifier.height(22.dp))

                Indicador(
                    titulo = "Câmeras online",
                    valor = camerasOnline,
                    simbolo = "▣"
                )

                Spacer(Modifier.height(22.dp))

                Indicador(
                    titulo = "Índice de segurança geral",
                    valor = indiceSeguranca,
                    simbolo = "♜"
                )

                Spacer(Modifier.height(24.dp))

                // GRÁFICO
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(440.dp)
                        .background(colorResource(R.color.Cartao), RoundedCornerShape(12.dp))
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .background(
                                Color(0xFF353535),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(4.dp)
                    ) {
                        listOf("Hoje", "Semana", "Mês").forEach { item ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (periodo == item)
                                            Color(0xFF484848)
                                        else Color.Transparent
                                    )
                                    .clickable { periodo = item }
                                    .padding(
                                        horizontal = 12.dp,
                                        vertical = 8.dp
                                    )
                            ) {
                                Text(
                                    text = item,
                                    color = colorResource(R.color.TextoClaro),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Ocorrências ao longo do tempo",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "Detecções de IA e registros manuais",
                        color = colorResource(R.color.TextoClaro),
                        fontSize = 12.sp
                    )

                    Spacer(Modifier.height(25.dp))

                    // Gráfico vazio, preparado para API
                    GraficoOcorrencias(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }

                Spacer(Modifier.height(30.dp))
            }
        }

        // MENU SOBREPOSTO AO DASHBOARD
        if (menuAberto) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(2f)
                    .background(Color.Black.copy(alpha = 0.35f))
                    .clickable { menuAberto = false }
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.78f)
                    .padding(top = 95.dp, bottom = 12.dp)
                    .zIndex(3f)
                    .background(colorResource(R.color.Cartao))
                    .border(2.dp,colorResource(R.color.Laranja))
                    .verticalScroll(rememberScrollState())
                    .clickable(enabled = false) {}
                    .padding(14.dp)
            ) {
                Spacer(Modifier.height(30.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(colorResource(R.color.Laranja), RoundedCornerShape(5.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "O",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 23.sp
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Text(
                        "CENTRAL",
                        color = colorResource(R.color.Salmao),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                Spacer(Modifier.height(32.dp))

                ItemMenu("▦", "DASHBOARD", true) {
                    menuAberto = false
                    onDashboardClick()
                }

                ItemMenu("▦", "SETORES") {
                    menuAberto = false
                    onSetoresClick()
                }

                ItemMenu("▣", "CÂMERAS") {
                    menuAberto = false
                    onCamerasClick()
                }

                ItemMenu("⚠", "OCORRÊNCIAS") {
                    menuAberto = false
                    onOcorrenciasClick()
                }

                ItemMenu("▤", "FUNCIONÁRIOS") {
                    menuAberto = false
                    onFuncionariosClick()
                }

                Spacer(Modifier.height(20.dp))

                // PERFIL ADMINISTRADOR
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Color.DarkGray,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        "ADMINISTRADOR · ver perfil",
                        modifier = Modifier.clickable { menuAberto = false; onPerfilClick() },
                        color = colorResource(R.color.TextoClaro),
                        fontSize = 12.sp
                    )

                    Spacer(Modifier.height(10.dp))

                    Button(
                        onClick = {
                            menuAberto = false
                            onSairClick()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF592323)
                        ),
                        shape = RoundedCornerShape(15.dp),
                        contentPadding = PaddingValues(
                            horizontal = 12.dp,
                            vertical = 4.dp
                        )
                    ) {
                        Text(
                            "sair da conta",
                            color = Color.White,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

// CARTÃO DE INDICADOR
@Composable
private fun Indicador(
    titulo: String,
    valor: String,
    simbolo: String,
    variacao: String? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(155.dp)
            .background(colorResource(R.color.Cartao), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        colorResource(R.color.Cartao),
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    simbolo,
                    color = colorResource(R.color.Salmao),
                    fontSize = 24.sp
                )
            }

            if (variacao != null) {
                Text(
                    text = variacao,
                    color = colorResource(R.color.Salmao),
                    fontSize = 12.sp
                )
            }
        }

        Column {
            Text(
                text = valor,
                color = Color(0xFFE1E1E1),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = titulo,
                color = colorResource(R.color.TextoClaro),
                fontSize = 13.sp
            )
        }
    }
}

// ITEM DO MENU
@Composable
private fun ItemMenu(
    simbolo: String,
    titulo: String,
    selecionado: Boolean = false,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = simbolo,
                color = if (selecionado) colorResource(R.color.Laranja) else colorResource(R.color.TextoClaro),
                fontSize = 21.sp
            )

            Spacer(Modifier.width(18.dp))

            Text(
                text = titulo,
                color = if (selecionado) colorResource(R.color.Laranja) else colorResource(R.color.TextoClaro),
                fontSize = 13.sp,
                fontWeight = if (selecionado)
                    FontWeight.Bold else FontWeight.Normal
            )
        }

        HorizontalDivider(
            color = Color(0xFF777777),
            thickness = 1.dp
        )
    }
}

// ÁREA DO GRÁFICO
@Composable
private fun GraficoOcorrencias(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val linhas = 4

                for (i in 0..linhas) {
                    val y = size.height * i / linhas

                    drawLine(
                        color = Color(0xFF343434),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }

            Text(
                text = "Aguardando dados das ocorrências",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Os dados serão exibidos após integração com a API",
            color = Color.Gray,
            fontSize = 10.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

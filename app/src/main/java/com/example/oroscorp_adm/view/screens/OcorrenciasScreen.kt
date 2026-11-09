package com.example.oroscorp_adm.view.screens

import com.example.oroscorp_adm.R
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OcorrenciasScreen(
    onBackClick: () -> Unit,
    onRegistrarClick: () -> Unit,
    ocorrencias: List<Any> = emptyList()
) {
    var paginaAtual by remember { mutableIntStateOf(1) }
    val itensPorPagina = 6
    var pesquisa by remember { mutableStateOf("") }

    val totalPaginas = maxOf(
        1,
        (ocorrencias.size + itensPorPagina - 1) / itensPorPagina
    )

    val pagina = paginaAtual.coerceIn(1, totalPaginas)
    val listaPagina = ocorrencias
        .drop((pagina - 1) * itensPorPagina)
        .take(itensPorPagina)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.Fundo))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
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
                            colorResource(R.color.Laranja),
                            RoundedCornerShape(18.dp)
                        )
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("←", color = colorResource(R.color.Laranja), fontSize = 34.sp)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp)
            ) {
                Spacer(Modifier.height(32.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(7.dp)
                            .height(30.dp)
                            .background(colorResource(R.color.Salmao), RoundedCornerShape(3.dp))
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = "OCORRÊNCIAS",
                        color = Color(0xFFE6E6E6),
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Registros de segurança gerados manualmente ou detectados pela IA.",
                    color = Color(0xFFE1C5B5),
                    fontSize = 15.sp,
                    lineHeight = 23.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(20.dp))

                // BOTÃO REGISTRAR
                Button(
                    onClick = { onRegistrarClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.Laranja)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .width(150.dp)
                        .height(44.dp)
                ) {
                    Text("+ Registrar", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Spacer(Modifier.height(24.dp))

                // CARDS DE ESTATÍSTICAS (Grid 2x2)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CardEstatistica("TOTAL", "--", Modifier.weight(1f))
                    CardEstatistica("EM ABERTO", "--", Modifier.weight(1f), corValor = colorResource(R.color.Salmao))
                }

                Spacer(Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CardEstatistica("DETECTADAS POR IA", "--", Modifier.weight(1f), corValor = colorResource(R.color.Salmao))
                    CardEstatistica("FECHADAS", "--", Modifier.weight(1f))
                }

                Spacer(Modifier.height(24.dp))

                // BLOCO DE FILTROS E BUSCA
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, colorResource(R.color.Borda), RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(colorResource(R.color.Cartao))
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = pesquisa,
                        onValueChange = { pesquisa = it },
                        placeholder = { Text("Buscar por ID, tipo ou funcio", color = Color.Gray, fontSize = 13.sp) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            unfocusedContainerColor = Color(0xFF1B1B1B),
                            focusedContainerColor = Color(0xFF1B1B1B),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        )
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        BotaoFiltro("Setor", Modifier.weight(1f))
                        BotaoFiltro("Funcionário", Modifier.weight(1f))
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        BotaoFiltro("Status", Modifier.weight(1f))
                        BotaoFiltro("Origem", Modifier.weight(1f))
                    }
                }

                Spacer(Modifier.height(24.dp))

                // TABELA DE OCORRÊNCIAS COM PAGINAÇÃO
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, colorResource(R.color.Borda), RoundedCornerShape(9.dp))
                        .clip(RoundedCornerShape(9.dp))
                        .background(colorResource(R.color.Cartao))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .padding(start = 22.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            "${ocorrencias.size} ocorrências",
                            color = Color(0xFFE1C5B5),
                            fontSize = 12.sp
                        )
                    }

                    HorizontalDivider(color = colorResource(R.color.Borda))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp)
                            .background(Color(0xFF1B1B1B))
                            .padding(horizontal = 18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "ID",
                            color = Color(0xFFE1C5B5),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(0.8f)
                        )
                        Text(
                            "FUNCIONÁRIO",
                            color = Color(0xFFE1C5B5),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1.5f)
                        )
                        Text(
                            "TIPO",
                            color = Color(0xFFE1C5B5),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1.5f)
                        )
                    }

                    if (listaPagina.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Nenhuma ocorrência encontrada",
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                        }
                    } else {
                        listaPagina.forEach { _ ->
                            LinhaOcorrenciaMock()
                            HorizontalDivider(
                                color = Color(0xFF303030),
                                thickness = 0.5.dp
                            )
                        }
                    }

                    HorizontalDivider(color = colorResource(R.color.Borda))

                    // CONTROLES DE PAGINAÇÃO
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BotaoPagina("Anterior", pagina > 1) {
                            paginaAtual = pagina - 1
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            (1..totalPaginas).forEach { numero ->
                                Box(
                                    modifier = Modifier
                                        .size(29.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            if (numero == pagina) colorResource(R.color.Salmao)
                                            else Color.Transparent
                                        )
                                        .clickable { paginaAtual = numero },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "$numero",
                                        color = if (numero == pagina)
                                            Color.Black else colorResource(R.color.Salmao),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        BotaoPagina("Próxima", pagina < totalPaginas) {
                            paginaAtual = pagina + 1
                        }
                    }
                }

                Spacer(Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun CardEstatistica(titulo: String, valor: String, modifier: Modifier = Modifier, corValor: Color = Color.White) {
    Column(
        modifier = modifier
            .height(110.dp)
            .background(colorResource(R.color.Cartao), RoundedCornerShape(14.dp))
            .border(1.dp, colorResource(R.color.Borda), RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(titulo, color = colorResource(R.color.TextoClaro), fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Text(valor, color = corValor, fontSize = 26.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun BotaoFiltro(texto: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(44.dp)
            .background(Color(0xFF1B1B1B), RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(texto, color = Color.White, fontSize = 13.sp)
        Text("▾", color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
private fun LinhaOcorrenciaMock() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp)
            .padding(horizontal = 18.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("#000000", color = colorResource(R.color.Salmao), fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.8f))
        Text("Aguardando API", color = Color.Gray, fontSize = 12.sp, fontStyle = FontStyle.Italic, modifier = Modifier.weight(1.5f))
        Text("Carregando...", color = Color.LightGray, fontSize = 12.sp, modifier = Modifier.weight(1.5f))
    }
}

@Composable
private fun BotaoPagina(
    texto: String,
    ativo: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = ativo,
        contentPadding = PaddingValues(horizontal = 8.dp),
        modifier = Modifier.height(32.dp),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(1.dp, colorResource(R.color.Borda)),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = colorResource(R.color.Salmao),
            disabledContentColor = Color.Gray
        )
    ) {
        Text(texto, fontSize = 11.sp)
    }
}
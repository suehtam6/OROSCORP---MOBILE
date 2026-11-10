
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OcorrenciasScreen(
    onBackClick: () -> Unit,
    onRegistrarClick: () -> Unit,
    onDetalhesClick: (DetalhesOcorrencia) -> Unit = {},
    ocorrencias: List<DetalhesOcorrencia> = emptyList()
) {
    var paginaAtual by remember { mutableIntStateOf(1) }
    var pesquisa by remember { mutableStateOf("") }
    var setorFiltro by remember { mutableStateOf("Todos") }
    var funcionarioFiltro by remember { mutableStateOf("Todos") }
    var statusFiltro by remember { mutableStateOf("Todos") }
    var origemFiltro by remember { mutableStateOf("Todos") }

    val itensPorPagina = 6

    val setores = listOf("Todos") +
            ocorrencias.map { it.setor }
                .filter { it.isNotBlank() }.distinct()

    val funcionarios = listOf("Todos") +
            ocorrencias.mapNotNull { it.funcionario }
                .filter { it.isNotBlank() }.distinct()

    val status = listOf("Todos") +
            ocorrencias.map { it.status }
                .filter { it.isNotBlank() }.distinct()

    val listaFiltrada = ocorrencias.filter { ocorrencia ->
        val busca = pesquisa.trim()

        val correspondePesquisa = busca.isEmpty() ||
                ocorrencia.id.toString().contains(busca, true) ||
                ocorrencia.tipo.contains(busca, true) ||
                (ocorrencia.funcionario?.contains(busca, true) == true)

        correspondePesquisa &&
                (setorFiltro == "Todos" || ocorrencia.setor == setorFiltro) &&
                (funcionarioFiltro == "Todos" ||
                        ocorrencia.funcionario == funcionarioFiltro) &&
                (statusFiltro == "Todos" || ocorrencia.status == statusFiltro) &&
                (origemFiltro == "Todos" ||
                        if (origemFiltro == "IA") ocorrencia.tipoIA
                        else !ocorrencia.tipoIA)
    }

    val totalPaginas = maxOf(
        1,
        (listaFiltrada.size + itensPorPagina - 1) / itensPorPagina
    )

    val pagina = paginaAtual.coerceIn(1, totalPaginas)

    val listaPagina = listaFiltrada
        .drop((pagina - 1) * itensPorPagina)
        .take(itensPorPagina)

    val totalAbertas = ocorrencias.count {
        !it.status.equals("Fechada", true)
    }

    val totalIA = ocorrencias.count { it.tipoIA }

    val totalFechadas = ocorrencias.count {
        it.status.equals("Fechada", true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.Fundo))
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
                Text(
                    "←",
                    color = colorResource(R.color.Laranja),
                    fontSize = 34.sp
                )
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
                        .background(
                            colorResource(R.color.Salmao),
                            RoundedCornerShape(3.dp)
                        )
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
                lineHeight = 23.sp
            )

            Spacer(Modifier.height(20.dp))

            // REGISTRAR OCORRÊNCIA
            Button(
                onClick = onRegistrarClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.Laranja)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .width(150.dp)
                    .height(44.dp)
            ) {
                Text(
                    "+ Registrar",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.height(24.dp))

            // ESTATÍSTICAS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CardEstatisticaOcorrencia(
                    "TOTAL",
                    ocorrencias.size.toString(),
                    Modifier.weight(1f)
                )

                CardEstatisticaOcorrencia(
                    "EM ABERTO",
                    totalAbertas.toString(),
                    Modifier.weight(1f),
                    colorResource(R.color.Salmao)
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CardEstatisticaOcorrencia(
                    "DETECTADAS POR IA",
                    totalIA.toString(),
                    Modifier.weight(1f),
                    colorResource(R.color.Salmao)
                )

                CardEstatisticaOcorrencia(
                    "FECHADAS",
                    totalFechadas.toString(),
                    Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))

            // FILTROS
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        colorResource(R.color.Borda),
                        RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorResource(R.color.Cartao))
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = pesquisa,
                    onValueChange = {
                        pesquisa = it
                        paginaAtual = 1
                    },
                    placeholder = {
                        Text(
                            "Buscar por ID, tipo ou funcionário",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF1B1B1B),
                        unfocusedContainerColor = Color(0xFF1B1B1B),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(Modifier.weight(1f)) {
                        FiltroOcorrencia(
                            "Setor",
                            setorFiltro,
                            setores
                        ) {
                            setorFiltro = it
                            paginaAtual = 1
                        }
                    }

                    Box(Modifier.weight(1f)) {
                        FiltroOcorrencia(
                            "Funcionário",
                            funcionarioFiltro,
                            funcionarios
                        ) {
                            funcionarioFiltro = it
                            paginaAtual = 1
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(Modifier.weight(1f)) {
                        FiltroOcorrencia(
                            "Status",
                            statusFiltro,
                            status
                        ) {
                            statusFiltro = it
                            paginaAtual = 1
                        }
                    }

                    Box(Modifier.weight(1f)) {
                        FiltroOcorrencia(
                            "Origem",
                            origemFiltro,
                            listOf("Todos", "Manual", "IA")
                        ) {
                            origemFiltro = it
                            paginaAtual = 1
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // TABELA DE OCORRÊNCIAS
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        colorResource(R.color.Borda),
                        RoundedCornerShape(9.dp)
                    )
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
                        "${listaFiltrada.size} ocorrências encontradas",
                        color = Color(0xFFE1C5B5),
                        fontSize = 12.sp
                    )
                }

                HorizontalDivider(
                    color = colorResource(R.color.Borda)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .background(Color(0xFF1B1B1B))
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CabecalhoTabelaOcorrencia(
                        "ID", Modifier.weight(0.8f)
                    )

                    CabecalhoTabelaOcorrencia(
                        "FUNCIONÁRIO", Modifier.weight(1.5f)
                    )

                    CabecalhoTabelaOcorrencia(
                        "TIPO", Modifier.weight(1.5f)
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
                    listaPagina.forEach { ocorrencia ->
                        LinhaOcorrencia(
                            ocorrencia = ocorrencia,
                            onClick = {
                                onDetalhesClick(ocorrencia)
                            }
                        )

                        HorizontalDivider(
                            color = Color(0xFF303030),
                            thickness = 0.5.dp
                        )
                    }
                }

                HorizontalDivider(
                    color = colorResource(R.color.Borda)
                )

                // PAGINAÇÃO
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BotaoPaginaOcorrencia(
                        "Anterior",
                        pagina > 1
                    ) {
                        paginaAtual = pagina - 1
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        (1..totalPaginas).forEach { numero ->
                            Box(
                                modifier = Modifier
                                    .size(29.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        if (numero == pagina)
                                            colorResource(R.color.Salmao)
                                        else Color.Transparent
                                    )
                                    .clickable {
                                        paginaAtual = numero
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "$numero",
                                    color = if (numero == pagina)
                                        Color.Black
                                    else colorResource(R.color.Salmao),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    BotaoPaginaOcorrencia(
                        "Próxima",
                        pagina < totalPaginas
                    ) {
                        paginaAtual = pagina + 1
                    }
                }
            }

            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
private fun LinhaOcorrencia(
    ocorrencia: DetalhesOcorrencia,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .heightIn(min = 65.dp)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "#${ocorrencia.id.toString().padStart(5, '0')}",
            color = colorResource(R.color.Salmao),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.8f)
        )

        Text(
            ocorrencia.funcionario ?: "Não identificado",
            color = Color(0xFFE1E1E1),
            fontSize = 12.sp,
            modifier = Modifier.weight(1.5f)
        )

        Column(
            modifier = Modifier.weight(1.5f)
        ) {
            Text(
                ocorrencia.tipo,
                color = Color.White,
                fontSize = 12.sp
            )

            Spacer(Modifier.height(4.dp))

            Text(
                if (ocorrencia.tipoIA) "IA" else "Manual",
                color = colorResource(R.color.Salmao),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun CardEstatisticaOcorrencia(
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier,
    corValor: Color = Color.White
) {
    Column(
        modifier = modifier
            .height(110.dp)
            .background(
                colorResource(R.color.Cartao),
                RoundedCornerShape(14.dp)
            )
            .border(
                1.dp,
                colorResource(R.color.Borda),
                RoundedCornerShape(14.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            titulo,
            color = colorResource(R.color.TextoClaro),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            valor,
            color = corValor,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CabecalhoTabelaOcorrencia(
    texto: String,
    modifier: Modifier = Modifier
) {
    Text(
        texto,
        color = Color(0xFFE1C5B5),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
private fun FiltroOcorrencia(
    titulo: String,
    selecionado: String,
    opcoes: List<String>,
    onSelecionar: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(
                    Color(0xFF1B1B1B),
                    RoundedCornerShape(10.dp)
                )
                .clickable { expandido = true }
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                if (selecionado == "Todos")
                    titulo
                else selecionado,
                color = Color.White,
                fontSize = 12.sp,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )

            Text(
                "▾",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        DropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            opcoes.forEach { opcao ->
                DropdownMenuItem(
                    text = { Text(opcao) },
                    onClick = {
                        onSelecionar(opcao)
                        expandido = false
                    }
                )
            }
        }
    }
}

@Composable
private fun BotaoPaginaOcorrencia(
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
        border = BorderStroke(
            1.dp,
            colorResource(R.color.Borda)
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = colorResource(R.color.Salmao),
            disabledContentColor = Color.Gray
        )
    ) {
        Text(texto, fontSize = 11.sp)
    }
}

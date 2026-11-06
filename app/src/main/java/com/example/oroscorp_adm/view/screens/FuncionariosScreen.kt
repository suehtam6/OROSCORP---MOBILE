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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oroscorp_adm.model.Funcionario

@Composable
fun FuncionariosScreen(
    onBackClick: () -> Unit,
    funcionarios: List<Funcionario> = emptyList()
) {
    var paginaAtual by remember { mutableIntStateOf(1) }
    val itensPorPagina = 6

    val totalPaginas = maxOf(
        1,
        (funcionarios.size + itensPorPagina - 1) / itensPorPagina
    )

    val pagina = paginaAtual.coerceIn(1, totalPaginas)
    val listaPagina = funcionarios
        .drop((pagina - 1) * itensPorPagina)
        .take(itensPorPagina)

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
                    text = "FUNCIONÁRIOS",
                    color = Color(0xFFE6E6E6),
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Cadastro de colaboradores, conformidade de EPI e histórico de ocorrências.",
                color = Color(0xFFE1C5B5),
                fontSize = 15.sp,
                lineHeight = 23.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

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
                        "${funcionarios.size} FUNCIONÁRIOS ENCONTRADOS",
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
                        "FUNCIONÁRIO",
                        color = Color(0xFFE1C5B5),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1.5f)
                    )

                    Text(
                        "CARGO",
                        color = Color(0xFFE1C5B5),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
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
                            "Nenhum funcionário encontrado",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    listaPagina.forEach { funcionario ->
                        LinhaFuncionario(funcionario)
                        HorizontalDivider(
                            color = Color(0xFF303030),
                            thickness = 0.5.dp
                        )
                    }
                }

                HorizontalDivider(color = colorResource(R.color.Borda))

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

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun LinhaFuncionario(funcionario: Funcionario) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 70.dp)
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1.5f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        funcionario.cor.copy(alpha = 0.25f),
                        RoundedCornerShape(5.dp)
                    )
                    .border(
                        1.dp,
                        funcionario.cor.copy(alpha = 0.6f),
                        RoundedCornerShape(5.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    funcionario.iniciais,
                    color = funcionario.cor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(12.dp))

            Text(
                funcionario.nome,
                color = Color(0xFFE1E1E1),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }

        Text(
            funcionario.cargo,
            color = Color(0xFFE1C5B5),
            fontSize = 13.sp,
            lineHeight = 18.sp,
            modifier = Modifier.weight(1f)
        )
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

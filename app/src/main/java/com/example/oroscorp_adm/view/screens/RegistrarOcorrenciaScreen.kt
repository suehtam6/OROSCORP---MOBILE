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

@Composable
fun RegistrarOcorrenciaScreen(
    onBackClick: () -> Unit,
    onRegistrarSucesso: () -> Unit = {}
) {
    var prioridadeSelecionada by remember { mutableStateOf("Média") }
    var descricao by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.Fundo))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // CABEÇALHO FIXO NO TOPO
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

            // CONTEÚDO COM SCROLL ATIVADO (Resolve o corte em baixo)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Spacer(Modifier.height(10.dp))

                // TÍTULO E SUBTÍTULO
                Text(
                    text = "Registrar\nocorrência manual",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 34.sp
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Ocorrência criada por um operador ou gestor. O funcionário já está identificado.",
                    color = colorResource(R.color.TextoClaro),
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(Modifier.height(24.dp))

                // CAMPOS DE SELEÇÃO
                CampoDropdown("Funcionário", "Selecionar funcionário")
                Spacer(Modifier.height(16.dp))

                CampoDropdown("Setor", "Selecionar setor")
                Spacer(Modifier.height(16.dp))

                CampoDropdown("Câmera", "Selecionar câmera")
                Spacer(Modifier.height(16.dp))

                CampoDropdown("Tipo de ocorrência", "Selecionar tipo")
                Spacer(Modifier.height(16.dp))

                CampoDropdown("EPI relacionado", "Selecionar EPI")
                Spacer(Modifier.height(16.dp))

                // DATA E HORÁRIO
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CampoDataHora("Data", "04/09/2026", "📅", Modifier.weight(1f))
                    CampoDataHora("Horário", "08:00", "🕒", Modifier.weight(1f))
                }

                Spacer(Modifier.height(20.dp))

                // PRIORIDADE
                Text("Prioridade", color = colorResource(R.color.TextoClaro), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        BotaoPrioridade("Baixa", prioridadeSelecionada == "Baixa", Modifier.weight(1f)) { prioridadeSelecionada = "Baixa" }
                        BotaoPrioridade("Média", prioridadeSelecionada == "Média", Modifier.weight(1f)) { prioridadeSelecionada = "Média" }
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        BotaoPrioridade("Alta", prioridadeSelecionada == "Alta", Modifier.weight(1f)) { prioridadeSelecionada = "Alta" }
                        BotaoPrioridade("Crítica", prioridadeSelecionada == "Crítica", Modifier.weight(1f)) { prioridadeSelecionada = "Crítica" }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // DESCRIÇÃO
                Text("Descrição", color = colorResource(R.color.TextoClaro), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    placeholder = {
                        Text("Descreva o que foi observado, o contexto\ne as ações tomadas...", color = Color.Gray, fontSize = 13.sp)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        unfocusedContainerColor = colorResource(R.color.Cartao),
                        focusedContainerColor = colorResource(R.color.Cartao),
                        unfocusedBorderColor = colorResource(R.color.Borda),
                        focusedBorderColor = colorResource(R.color.Laranja)
                    )
                )

                Spacer(Modifier.height(20.dp))

                // EVIDÊNCIA
                Text("Evidência", color = colorResource(R.color.TextoClaro), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .border(1.dp, colorResource(R.color.Borda), RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(colorResource(R.color.Cartao))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🖼", fontSize = 24.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Clique para enviar uma imagem\nde evidência",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(Modifier.height(30.dp))

                // BOTÃO REGISTRAR OCORRÊNCIA
                Button(
                    onClick = { onRegistrarSucesso() },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.Laranja)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        "Registrar ocorrência",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                // Espaço extra no final para o scroll passar livremente da barra do sistema
                Spacer(Modifier.height(50.dp))
            }
        }
    }
}

@Composable
private fun CampoDropdown(titulo: String, placeholder: String) {
    Column {
        Text(titulo, color = colorResource(R.color.TextoClaro), fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(colorResource(R.color.Cartao), RoundedCornerShape(12.dp))
                .border(1.dp, colorResource(R.color.Borda), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(placeholder, color = Color.Gray, fontSize = 13.sp)
            Text("▾", color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
private fun CampoDataHora(titulo: String, valor: String, icone: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(titulo, color = colorResource(R.color.TextoClaro), fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(colorResource(R.color.Cartao), RoundedCornerShape(12.dp))
                .border(1.dp, colorResource(R.color.Borda), RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(valor, color = Color.White, fontSize = 13.sp)
            Text(icone, fontSize = 14.sp)
        }
    }
}

@Composable
private fun BotaoPrioridade(texto: String, selecionado: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(44.dp)
            .background(colorResource(R.color.Cartao), RoundedCornerShape(10.dp))
            .border(
                1.dp,
                if (selecionado) colorResource(R.color.Laranja) else colorResource(R.color.Borda),
                RoundedCornerShape(10.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = if (selecionado) colorResource(R.color.Laranja) else Color.White,
            fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal,
            fontSize = 13.sp
        )
    }
}
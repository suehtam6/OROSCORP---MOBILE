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
fun CamerasScreen(
    onBackClick: () -> Unit,
    cameras: List<Any> = emptyList() // Lista vinda da API. Se vazia, exibe o estado vazio.
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.Fundo))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // CABEÇALHO FIXO
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

            // CONTEÚDO COM SCROLL VERTICAL
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(10.dp))

                // TÍTULO E SUBTÍTULO
                Text(
                    text = "Câmeras",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Monitoramento em tempo real dos setores",
                    color = colorResource(R.color.TextoClaro),
                    fontSize = 13.sp,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "${cameras.size} câmeras online",
                    color = colorResource(R.color.Salmao),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(Modifier.height(24.dp))

                // SE A LISTA ESTIVER VAZIA (Aguardando API / Sem câmeras cadastradas)
                if (cameras.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .background(colorResource(R.color.Cartao), RoundedCornerShape(16.dp))
                            .border(1.dp, colorResource(R.color.Borda), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📹", fontSize = 36.sp)
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "Nenhuma câmera cadastrada",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "As câmeras aparecerão aqui após o cadastro na API.",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                } else {
                    // QUANDO HOUVER CÂMERAS, EXIBE OS CARDS COM SCROLL AUTOMÁTICO
                    cameras.forEach { camera ->
                        // Substitua pelos dados reais vindos da sua API quando modelar o objeto de câmera
                        CardCamera(
                            nomeCamera = "Câmera",
                            setor = "Setor monitorado",
                            ip = "0.0.0.0"
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun CardCamera(
    nomeCamera: String,
    setor: String,
    ip: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.Cartao), RoundedCornerShape(16.dp))
            .border(1.dp, colorResource(R.color.Borda), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        // VÍDEO / IMAGEM DA CÂMERA
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1B1B1B)),
            contentAlignment = Alignment.Center
        ) {
            Text("🖼", fontSize = 32.sp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .align(Alignment.TopStart),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TagStatus("● ONLINE")
                TagStatus("● AO VIVO")
            }
        }

        Spacer(Modifier.height(14.dp))

        // INFORMAÇÕES DA CÂMERA
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = nomeCamera,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = ip,
                color = Color.Gray,
                fontSize = 11.sp
            )
        }

        Spacer(Modifier.height(2.dp))

        Text(
            text = setor,
            color = colorResource(R.color.TextoClaro),
            fontSize = 13.sp
        )
    }
}

@Composable
private fun TagStatus(texto: String) {
    Box(
        modifier = Modifier
            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
            .border(0.5.dp, colorResource(R.color.Salmao).copy(alpha = 0.5f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = colorResource(R.color.Salmao),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
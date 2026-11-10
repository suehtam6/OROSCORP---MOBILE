
package com.example.oroscorp_adm.view.screens

import com.example.oroscorp_adm.R
import com.example.oroscorp_adm.model.Camera
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun CamerasScreen(
    onBackClick: () -> Unit,
    cameras: List<Camera> = emptyList(),
    onCameraClick: (Int) -> Unit = {}
) {
    val online = cameras.count { it.online }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.Fundo))
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Câmeras",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = "Monitoramento em tempo real dos setores",
                color = colorResource(R.color.TextoClaro),
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = "$online de ${cameras.size} câmeras online",
                color = colorResource(R.color.Salmao),
                fontSize = 12.sp
            )

            Spacer(Modifier.height(25.dp))

            if (cameras.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(
                            colorResource(R.color.Cartao),
                            RoundedCornerShape(14.dp)
                        )
                        .border(
                            1.dp,
                            colorResource(R.color.Borda),
                            RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            "Nenhuma câmera cadastrada",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            "Aguardando dados da API.",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                cameras.forEachIndexed { index, camera ->
                    CardCamera(
                        camera = camera,
                        onClick = { onCameraClick(index) }
                    )

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun CardCamera(
    camera: Camera,
    onClick: () -> Unit
) {
    val salmao = colorResource(R.color.Salmao)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(R.color.Cartao))
            .border(
                1.dp,
                colorResource(R.color.Borda),
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(155.dp)
                .background(Color(0xFF14141E)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (camera.online)
                    "VISUALIZAR CÂMERA"
                else
                    "SEM SINAL",
                color = if (camera.online)
                    salmao else Color.Gray,
                fontSize = 13.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatusCamera(
                    if (camera.online) "● ONLINE"
                    else "● OFFLINE"
                )

                if (camera.online) {
                    StatusCamera("● AO VIVO")
                }
            }
        }

        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                camera.nome,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    camera.setor,
                    color = colorResource(R.color.TextoClaro),
                    fontSize = 12.sp
                )

                Text(
                    camera.ip,
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            }

            Spacer(Modifier.height(15.dp))

            HorizontalDivider(
                color = colorResource(R.color.Borda)
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "${camera.pessoas ?: "-"} pessoas",
                    color = Color.LightGray,
                    fontSize = 11.sp
                )

                Text(
                    "${camera.alertas ?: "-"} alertas",
                    color = salmao,
                    fontSize = 11.sp
                )

                Text(
                    "${camera.ocorrencias ?: "-"} ocorr.",
                    color = Color.LightGray,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun StatusCamera(texto: String) {
    Box(
        modifier = Modifier
            .background(
                Color.Black.copy(alpha = 0.75f),
                RoundedCornerShape(4.dp)
            )
            .border(
                0.5.dp,
                Color(0xFFFF9966),
                RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 7.dp, vertical = 4.dp)
    ) {
        Text(
            texto,
            color = Color.White,
            fontSize = 10.sp
        )
    }
}

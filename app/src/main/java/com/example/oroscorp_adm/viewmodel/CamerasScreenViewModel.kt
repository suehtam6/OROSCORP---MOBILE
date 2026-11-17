
package com.example.oroscorp_adm.viewmodel

import com.example.oroscorp_adm.R
import com.example.oroscorp_adm.model.Camera
import com.example.oroscorp_adm.model.StatusCamera

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CamerasScreen(
    onBackClick: () -> Unit,
    cameras: List<Camera> = emptyList(),
    statusCamera: StatusCamera,
    statusCameras: List<StatusCamera>,
    onCameraClick: (Int) -> Unit = {}
) {
    val laranja = colorResource(R.color.Laranja)
    val salmao = colorResource(R.color.Salmao)
    val textoClaro = colorResource(R.color.TextoClaro)
    val cartao = colorResource(R.color.Cartao)
    val borda = colorResource(R.color.Borda)

    var pesquisa by remember { mutableStateOf("") }
    var setorSelecionado by remember { mutableStateOf<Int?>(null) }
    var menuSetoresAberto by remember { mutableStateOf(false) }

    // Setores encontrados nas câmeras recebidas da API.
    // Nenhum setor é cadastrado manualmente nesta tela.
    val setores = cameras
        .map { it.idSetor }
        .distinct()
        .sortedBy { it.toString() }

    // Mantém o índice original para abrir a câmera correta
    // mesmo quando a lista estiver filtrada.
    val camerasFiltradas = cameras.withIndex().filter { item ->
        val camera = item.value

        val correspondePesquisa =
            pesquisa.isBlank() ||
                    camera.nome.contains(
                        pesquisa.trim(),
                        ignoreCase = true
                    ) ||
                    camera.idSetor.toString().contains(
                        pesquisa.trim(),
                        ignoreCase = true
                    )

        val correspondeSetor =
            setorSelecionado == null ||
                    camera.idSetor == setorSelecionado

        correspondePesquisa && correspondeSetor
    }


    val camerasOnline = cameras.indices.count { index ->
        val status = statusCameras.getOrNull(index) ?: statusCamera

        status.status.equals(
            "online",
            ignoreCase = true
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.Fundo))
    ) {

        // CABEÇALHO ORIGINAL
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
                        laranja,
                        RoundedCornerShape(18.dp)
                    )
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "←",
                    color = laranja,
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
                color = textoClaro,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = "$camerasOnline câmeras online",
                color = salmao,
                fontSize = 12.sp
            )

            Spacer(Modifier.height(25.dp))

            // PESQUISA POR CÂMERA
            OutlinedTextField(
                value = pesquisa,
                onValueChange = { pesquisa = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Pesquisar câmera...",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Text(
                        text = "⌕",
                        color = laranja,
                        fontSize = 27.sp
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = laranja,
                    unfocusedBorderColor = borda,
                    focusedContainerColor = cartao,
                    unfocusedContainerColor = cartao,
                    cursorColor = laranja
                )
            )

            Spacer(Modifier.height(14.dp))

            // FILTRO DE CÂMERAS POR SETOR
            ExposedDropdownMenuBox(
                expanded = menuSetoresAberto,
                onExpandedChange = {
                    menuSetoresAberto = !menuSetoresAberto
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = setorSelecionado?.let {
                        "Setor $it"
                    } ?: "Todos os setores",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = {
                        Text("Filtrar por setor")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = menuSetoresAberto
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = laranja,
                        unfocusedBorderColor = borda,
                        focusedContainerColor = cartao,
                        unfocusedContainerColor = cartao,
                        focusedLabelColor = laranja,
                        unfocusedLabelColor = textoClaro
                    )
                )

                ExposedDropdownMenu(
                    expanded = menuSetoresAberto,
                    onDismissRequest = {
                        menuSetoresAberto = false
                    },
                    modifier = Modifier.background(cartao)
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Todos os setores",
                                color = Color.White
                            )
                        },
                        onClick = {
                            setorSelecionado = null
                            menuSetoresAberto = false
                        }
                    )

                    setores.forEach { setor ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Setor $setor",
                                    color = Color.White
                                )
                            },
                            onClick = {
                                setorSelecionado = setor
                                menuSetoresAberto = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${camerasFiltradas.size} câmeras encontradas",
                    color = textoClaro,
                    fontSize = 12.sp
                )

                if (pesquisa.isNotBlank() || setorSelecionado != null) {
                    Text(
                        text = "Limpar filtros",
                        color = laranja,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            pesquisa = ""
                            setorSelecionado = null
                        }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // LISTA DE CÂMERAS
            if (cameras.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(
                            cartao,
                            RoundedCornerShape(14.dp)
                        )
                        .border(
                            1.dp,
                            borda,
                            RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Nenhuma câmera cadastrada",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = "Aguardando dados da API.",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            } else if (camerasFiltradas.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            cartao,
                            RoundedCornerShape(14.dp)
                        )
                        .border(
                            1.dp,
                            borda,
                            RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Nenhuma câmera encontrada",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Tente alterar a pesquisa ou o setor.",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                camerasFiltradas.forEach { item ->
                    val indexOriginal = item.index
                    val camera = item.value

                    val statusDaCamera =
                        statusCameras.getOrNull(indexOriginal)
                            ?: statusCamera

                    CardCamera(
                        camera = camera,
                        statusCamera = statusDaCamera,
                        onClick = {
                            onCameraClick(indexOriginal)
                        }
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
    statusCamera: StatusCamera,
    onClick: () -> Unit
) {
    val salmao = colorResource(R.color.Salmao)

    val online = statusCamera.status.equals(
        "online",
        ignoreCase = true
    )

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
                text = if (online) {
                    "VISUALIZAR CÂMERA"
                } else {
                    "SEM SINAL"
                },
                color = if (online) salmao else Color.Gray,
                fontSize = 13.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                EtiquetaStatusCamera(
                    if (online) "● ONLINE" else "● OFFLINE"
                )

                if (online) {
                    EtiquetaStatusCamera("● AO VIVO")
                }
            }
        }

        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = camera.nome,
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
                    text = "Setor ${camera.idSetor}",
                    color = colorResource(R.color.TextoClaro),
                    fontSize = 12.sp
                )

                Text(
                    text = camera.nome,
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
                    text = "",
                    color = Color.LightGray,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun EtiquetaStatusCamera(texto: String) {
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
            .padding(
                horizontal = 7.dp,
                vertical = 4.dp
            )
    ) {
        Text(
            text = texto,
            color = Color.White,
            fontSize = 10.sp
        )
    }
}

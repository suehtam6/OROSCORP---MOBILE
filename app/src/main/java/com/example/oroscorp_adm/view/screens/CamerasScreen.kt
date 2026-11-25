
package com.example.oroscorp_adm.view.screens

import com.example.oroscorp_adm.ui.theme.TemaGlobal
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
    statusCamera: StatusCamera = StatusCamera(-1, "Indisponível"),
    statusCameras: List<StatusCamera> = emptyList(),
    onCameraClick: (Int) -> Unit = {}
) {
    val laranja = TemaGlobal.cor(colorResource(R.color.Laranja), Color(0xFFB85A00))
    val salmao = TemaGlobal.cor(colorResource(R.color.Salmao), Color(0xFFAD501B))
    val textoClaro = TemaGlobal.cor(colorResource(R.color.TextoClaro), Color(0xFF65574F))
    val cartao = TemaGlobal.cor(colorResource(R.color.Cartao), Color(0xFFFFFFFF))
    val borda = TemaGlobal.cor(colorResource(R.color.Borda), Color(0xFF9CA3AF))

    var pesquisa by remember { mutableStateOf("") }
    var setorSelecionado by remember { mutableStateOf<Int?>(null) }
    var menuSetoresAberto by remember { mutableStateOf(false) }

    // Setores encontrados nas câmeras recebidas da API.
    // Nenhum setor é cadastrado manualmente nesta tela.
    val setores = cameras
        .map { it.id_setor }
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
                    camera.id_setor.toString().contains(
                        pesquisa.trim(),
                        ignoreCase = true
                    )

        val correspondeSetor =
            setorSelecionado == null ||
                    camera.id_setor == setorSelecionado

        correspondePesquisa && correspondeSetor
    }


    val camerasOnline = cameras.indices.count { index ->
        val status = statusCameras.firstOrNull { it.id == cameras[index].id_status_camera } ?: statusCamera

        status.status.equals(
            "online",
            ignoreCase = true
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TemaGlobal.cor(colorResource(R.color.Fundo), Color(0xFFF2F3F5)))
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
                    .height(75.dp).background(Color.Black, RoundedCornerShape(12.dp)),
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
                color = TemaGlobal.cor(Color.White, Color(0xFF191C1F)),
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
                        color = TemaGlobal.cor(Color.Gray, Color(0xFF60656B)),
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
                    focusedTextColor = TemaGlobal.cor(Color.White, Color(0xFF191C1F)),
                    unfocusedTextColor = TemaGlobal.cor(Color.White, Color(0xFF191C1F)),
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
                        cameras.firstOrNull { cam -> cam.id_setor == it }?.nome_setor ?: "Setor $it"
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
                        focusedTextColor = TemaGlobal.cor(Color.White, Color(0xFF191C1F)),
                        unfocusedTextColor = TemaGlobal.cor(Color.White, Color(0xFF191C1F)),
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
                                color = TemaGlobal.cor(Color.White, Color(0xFF191C1F))
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
                                    text = cameras.firstOrNull { it.id_setor == setor }?.nome_setor ?: "Setor $setor",
                                    color = TemaGlobal.cor(Color.White, Color(0xFF191C1F))
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
                            color = TemaGlobal.cor(Color.White, Color(0xFF191C1F)),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = "Aguardando dados da API.",
                            color = TemaGlobal.cor(Color.Gray, Color(0xFF60656B)),
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
                            color = TemaGlobal.cor(Color.White, Color(0xFF191C1F)),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Tente alterar a pesquisa ou o setor.",
                            color = TemaGlobal.cor(Color.Gray, Color(0xFF60656B)),
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                camerasFiltradas.forEach { item ->
                    val indexOriginal = item.index
                    val camera = item.value

                    val statusDaCamera =
                        statusCameras.firstOrNull { it.id == camera.id_status_camera }
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
    val salmao = TemaGlobal.cor(colorResource(R.color.Salmao), Color(0xFFAD501B))

    val online = statusCamera.status.equals(
        "online",
        ignoreCase = true
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(TemaGlobal.cor(colorResource(R.color.Cartao), Color(0xFFFFFFFF)))
            .border(
                1.dp,
                TemaGlobal.cor(colorResource(R.color.Borda), Color(0xFF9CA3AF)),
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(155.dp)
                .background(TemaGlobal.cor(Color(0xFF14141E), Color(0xFFE9EBEE))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (online) "VISUALIZAR CÂMERA"
                    else if (statusCamera.id < 0) "STATUS INDISPONÍVEL" else "SEM SINAL",
                color = if (online) salmao else TemaGlobal.cor(Color.Gray, Color(0xFF60656B)),
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
                    if (statusCamera.id < 0) "● INDISPONÍVEL" else if (online) "● ONLINE" else "● OFFLINE"
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
                color = TemaGlobal.cor(Color.White, Color(0xFF191C1F)),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = camera.nome_setor ?: "Setor ${camera.id_setor}",
                    color = TemaGlobal.cor(colorResource(R.color.TextoClaro), Color(0xFF65574F)),
                    fontSize = 12.sp
                )

                Text(
                    text = camera.endereco_stream.substringAfter("://").substringAfterLast("@").substringBefore("/"),
                    color = TemaGlobal.cor(Color.Gray, Color(0xFF60656B)),
                    fontSize = 11.sp
                )
            }

            Spacer(Modifier.height(15.dp))

            HorizontalDivider(
                color = TemaGlobal.cor(colorResource(R.color.Borda), Color(0xFF9CA3AF))
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "",
                    color = TemaGlobal.cor(Color.LightGray, Color(0xFF60656B)),
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


package com.example.oroscorp_adm.view.screens

import com.example.oroscorp_adm.ui.theme.TemaGlobal
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.oroscorp_adm.R
import com.example.oroscorp_adm.model.Usuario

@Composable
fun PerfilAdminScreen(
    usuario: Usuario?,
    onBackClick: () -> Unit
) {

    val laranja = TemaGlobal.cor(
        Color(0xFFFF7900),
        Color(0xFFB85A00)
    )

    Column(
        Modifier
            .fillMaxSize()
            .background(
                TemaGlobal.cor(
                    Color(0xFF353535),
                    Color(0xFFF2F3F5)
                )
            )
    ) {

        // HEADER

        Row(
            Modifier
                .fillMaxWidth()
                .height(95.dp)
                .background(Color.Black)
                .padding(12.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(R.drawable.logo),

                contentDescription = "Logo OROSCORP",

                modifier = Modifier
                    .weight(1f)
                    .height(75.dp)
                    .background(
                        Color.Black,
                        RoundedCornerShape(12.dp)
                    ),

                contentScale = ContentScale.Fit
            )

            Box(
                Modifier
                    .size(55.dp)
                    .border(
                        2.dp,
                        laranja,
                        RoundedCornerShape(16.dp)
                    )
                    .clickable(onClick = onBackClick),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    "←",
                    color = laranja,
                    fontSize = 33.sp
                )
            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(28.dp))

            // FOTO DO PERFIL

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        TemaGlobal.cor(
                            Color(0xFF171717),
                            Color(0xFFFFFFFF)
                        ),
                        RoundedCornerShape(16.dp)
                    )
                    .then(
                        if (TemaGlobal.modoClaro)
                            Modifier.border(
                                1.dp,
                                TemaGlobal.borda,
                                RoundedCornerShape(16.dp)
                            )
                        else Modifier
                    ),

                contentAlignment = Alignment.Center
            ) {

                if (!usuario?.foto.isNullOrBlank()) {

                    coil.compose.AsyncImage(
                        model = usuario?.foto,

                        contentDescription = "Foto do perfil",

                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                color = laranja,
                                shape = CircleShape
                            ),

                        contentScale = ContentScale.Crop,

                        alignment = Alignment.Center
                    )

                } else {

                    Text(
                        usuario?.nome
                            ?.split(" ")
                            ?.take(2)
                            ?.joinToString("") {
                                it.take(1).uppercase()
                            }
                            ?.ifBlank { "—" } ?: "—",

                        color = laranja,

                        fontSize = 55.sp
                    )
                }
            }

            Spacer(Modifier.height(42.dp))

            // INFORMAÇÕES DO USUÁRIO

            Column(
                Modifier
                    .fillMaxWidth()
                    .background(
                        TemaGlobal.cor(
                            Color(0xFF171717),
                            Color(0xFFFFFFFF)
                        ),
                        RoundedCornerShape(16.dp)
                    )
                    .then(
                        if (TemaGlobal.modoClaro)
                            Modifier.border(
                                1.dp,
                                TemaGlobal.borda,
                                RoundedCornerShape(16.dp)
                            )
                        else Modifier
                    )
                    .padding(20.dp)
            ) {

                Text(
                    "INFORMAÇÕES",
                    color = laranja,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    "Nome: ${usuario?.nome?.ifBlank { "—" } ?: "—"}",

                    color = TemaGlobal.cor(
                        Color.White,
                        Color(0xFF191C1F)
                    )
                )

                Text(
                    "E-mail: ${usuario?.email?.ifBlank { "—" } ?: "—"}",

                    color = TemaGlobal.cor(
                        Color.White,
                        Color(0xFF191C1F)
                    )
                )

                Text(
                    "Nascimento: ${usuario?.data_nascimento?.ifBlank { "—" } ?: "—"}",

                    color = TemaGlobal.cor(
                        Color.White,
                        Color(0xFF191C1F)
                    )
                )
            }

            Spacer(Modifier.height(18.dp))

            if (usuario == null) {

                Text(
                    "Os dados do perfil não foram retornados pelo endpoint de login.",

                    color = TemaGlobal.cor(
                        Color.LightGray,
                        Color(0xFF60656B)
                    ),

                    fontSize = 13.sp
                )
            }
        }
    }
}

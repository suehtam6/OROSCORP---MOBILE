package com.example.oroscorp_adm.view.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.oroscorp_adm.R
import com.example.oroscorp_adm.model.Usuario

@Composable
fun PerfilAdminScreen(usuario: Usuario?, onBackClick: () -> Unit) {
    val laranja = Color(0xFFFF7900)
    Column(Modifier.fillMaxSize().background(Color(0xFF353535))) {
        Row(Modifier.fillMaxWidth().height(95.dp).background(Color.Black).padding(12.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Image(painterResource(R.drawable.logo), "Logo OROSCORP",
                Modifier.weight(1f).height(75.dp), contentScale = ContentScale.Fit)
            Box(Modifier.size(55.dp).border(2.dp, laranja, RoundedCornerShape(16.dp))
                .clickable(onClick = onBackClick), contentAlignment = Alignment.Center) {
                Text("←", color = laranja, fontSize = 33.sp)
            }
        }
        Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(28.dp))
            Box(Modifier.fillMaxWidth().height(180.dp).background(Color(0xFF171717), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center) {
                Text(usuario?.nome?.split(" ")?.take(2)?.joinToString("") { it.take(1).uppercase() }
                    ?.ifBlank { "—" } ?: "—", color = laranja, fontSize = 55.sp)
            }
            Spacer(Modifier.height(42.dp))
            Column(Modifier.fillMaxWidth().background(Color(0xFF171717), RoundedCornerShape(16.dp)).padding(20.dp)) {
                Text("INFORMAÇÕES", color = laranja, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                Text("Nome: ${usuario?.nome?.ifBlank { "—" } ?: "—"}", color = Color.White)
                Text("E-mail: ${usuario?.email?.ifBlank { "—" } ?: "—"}", color = Color.White)
                Text("Nascimento: ${usuario?.data_nascimento?.ifBlank { "—" } ?: "—"}", color = Color.White)
            }
            Spacer(Modifier.height(18.dp))
            if (usuario == null) {
                Text("Os dados do perfil não foram retornados pelo endpoint de login.",
                    color = Color.LightGray, fontSize = 13.sp)
            }
        }
    }
}

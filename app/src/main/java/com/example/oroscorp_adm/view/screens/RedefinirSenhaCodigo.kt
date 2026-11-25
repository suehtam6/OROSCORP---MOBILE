package com.example.oroscorp_adm.view.screens

import com.example.oroscorp_adm.ui.theme.TemaGlobal
import androidx.compose.foundation.background
import com.example.oroscorp_adm.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RedefinirSenhaCodigo(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onValidarCodigo: ((String) -> Unit)? = null,
    erro: String? = null,
    carregando: Boolean = false // Parâmetro para ação de continuar
) {
    var codigo by remember { mutableStateOf("") } // Ajustado de email para codigo

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 1. Imagem de Fundo
        if (!TemaGlobal.modoClaro) {
        Image(
            painter = painterResource(id = R.drawable.fundo),
            contentDescription = "Fundo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        } else {
            Box(Modifier.fillMaxSize().background(TemaGlobal.fundo))
        }

        // 2. Conteúdo da Tela
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Logo Oroscorp no topo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Oroscorp",
                modifier = Modifier
                    .width(180.dp)
                    .height(50.dp).background(Color.Black, RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(100.dp))

            // Instrução
            Text(
                text = "DIGITE O CÓDIGO PARA REDEFINIÇÃO DA SENHA",
                color = TemaGlobal.cor(Color.White, Color(0xFF191C1F)),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Caixa de Texto (Código)
            OutlinedTextField(
                value = codigo,
                onValueChange = { codigo = it },
                placeholder = { Text("CÓDIGO", color = TemaGlobal.cor(Color.Gray, Color(0xFF60656B))) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Color(0xFFF5F3F3),
                    focusedContainerColor = Color(0xFFF5F3F3),
                    unfocusedBorderColor = TemaGlobal.cor(Color.Transparent, TemaGlobal.borda),
                    focusedBorderColor = TemaGlobal.cor(Color.Transparent, TemaGlobal.laranja)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botão Continue
            Button(
                onClick = { if (onValidarCodigo != null) onValidarCodigo(codigo) else onContinueClick() },
                enabled = !carregando && codigo.isNotBlank(), // Aciona a ação de continuar
                modifier = Modifier
                    .width(160.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF5F3F3)
                )
            ) {
                Text(
                    text = "CONTINUE",
                    color = TemaGlobal.cor(Color(0xFFFF7200), Color(0xFFB85A00)),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            TextButton(onClick = onBackClick, enabled = !carregando) { Text("VOLTAR", color = TemaGlobal.cor(Color.White, Color(0xFF191C1F))) }
            if (!erro.isNullOrBlank()) {
                Spacer(Modifier.height(16.dp))
                Text(erro, color = TemaGlobal.cor(Color(0xFFFF9E85), Color(0xFFB3261E)), fontSize = 12.sp)
            }
        }
    }
}
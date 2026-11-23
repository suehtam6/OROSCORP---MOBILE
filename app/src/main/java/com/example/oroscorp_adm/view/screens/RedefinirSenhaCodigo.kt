package com.example.oroscorp_adm.view.screens

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
        Image(
            painter = painterResource(id = R.drawable.fundo),
            contentDescription = "Fundo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

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
                    .height(50.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(100.dp))

            // Instrução
            Text(
                text = "DIGITE O CÓDIGO PARA REDEFINIÇÃO DA SENHA",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Caixa de Texto (Código)
            OutlinedTextField(
                value = codigo,
                onValueChange = { codigo = it },
                placeholder = { Text("CÓDIGO", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF5F3F3),
                    focusedContainerColor = Color(0xFFF5F3F3),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
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
                    color = Color(0xFFFF7200),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            TextButton(onClick = onBackClick, enabled = !carregando) { Text("VOLTAR", color = Color.White) }
            if (!erro.isNullOrBlank()) {
                Spacer(Modifier.height(16.dp))
                Text(erro, color = Color(0xFFFF9E85), fontSize = 12.sp)
            }
        }
    }
}
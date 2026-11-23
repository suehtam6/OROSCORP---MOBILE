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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TrocarSenhaScreen(
    onBackClick: () -> Unit,
    onRedefinirClick: () -> Unit,
    onConfirmarSenha: ((String) -> Unit)? = null,
    erro: String? = null,
    carregando: Boolean = false
) {
    var novaSenha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }

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

            Spacer(modifier = Modifier.height(80.dp))

            // Instrução
            Text(
                text = "DIGITE SUA NOVA SENHA PARA\nPODERMOS REDEFINIR\nSUA SENHA",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Caixa de Texto (Senha)
            OutlinedTextField(
                value = novaSenha,
                onValueChange = { novaSenha = it },
                placeholder = { Text("Senha", color = Color.Gray) },
                visualTransformation = PasswordVisualTransformation(),
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

            Spacer(modifier = Modifier.height(16.dp))

            // Caixa de Texto (Confirmar Senha)
            OutlinedTextField(
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it },
                placeholder = { Text("Confirmar Senha", color = Color.Gray) },
                visualTransformation = PasswordVisualTransformation(),
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

            Spacer(modifier = Modifier.height(24.dp))

            // Botão Redefinir
            Button(
                onClick = { if (novaSenha == confirmarSenha && novaSenha.isNotBlank()) {
                    if (onConfirmarSenha != null) onConfirmarSenha(novaSenha) else onRedefinirClick()
                } },
                enabled = !carregando && novaSenha.isNotBlank() && novaSenha == confirmarSenha,
                modifier = Modifier
                    .width(160.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF5F3F3)
                )
            ) {
                Text(
                    text = "REDEFINIR",
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
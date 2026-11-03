package com.example.oroscorp_adm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oroscorp_adm.screens.EsqueceuSenhaScreen
import com.example.oroscorp_adm.ui.theme.OroscorpadmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OroscorpadmTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showEsqueceuSenha by remember { mutableStateOf(false) }

    if (showEsqueceuSenha) {
        EsqueceuSenhaScreen(onBackClick = { showEsqueceuSenha = false })
    } else {
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

            // 2. Conteúdo da Tela de Login
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

                // Título
                Text(
                    text = "ENTRE NA SUA CONTA",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Campo de E-mail
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("E-mail", color = Color.Gray) },
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

                // Campo de Senha
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("SENHA", color = Color.Gray) },
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

                Spacer(modifier = Modifier.height(12.dp))

                // Botão "Esqueceu sua senha?"
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    TextButton(onClick = { showEsqueceuSenha = true }) {
                        Text(
                            text = "Esqueceu sua senha?",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botão ENTRAR
                Button(
                    onClick = { /* Ação de login */ },
                    modifier = Modifier
                        .width(160.dp)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF5F3F3)
                    )
                ) {
                    Text(
                        text = "ENTRAR",
                        color = Color(0xFFFF7200),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Rodapé Cadastre-se
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Não tem conta? ",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    TextButton(onClick = { /* Ação de cadastro */ }) {
                        Text(
                            text = "Cadastre-se",
                            color = Color(0xFFFF7200),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
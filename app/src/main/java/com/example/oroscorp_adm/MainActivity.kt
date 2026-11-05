
package com.example.oroscorp_adm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.oroscorp_adm.screens.HomeScreen
import com.example.oroscorp_adm.screens.RedefinirSenhaCodigo
import com.example.oroscorp_adm.screens.TrocarSenhaScreen
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
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {

    // Controla qual tela está sendo exibida
    var telaAtual by remember { mutableStateOf("login") }

    when (telaAtual) {

        // TELA DE LOGIN
        "login" -> {
            LoginScreen(
                onEntrarClick = {
                    telaAtual = "home"
                },
                onEsqueceuSenhaClick = {
                    telaAtual = "esqueceu"
                }
            )
        }

        // TELA HOME / DASHBOARD
        "home" -> {
            HomeScreen(
                onDashboardClick = {
                    telaAtual = "home"
                },
                onSetoresClick = {
                    // Adicionar navegação para Setores
                },
                onCamerasClick = {
                    // Adicionar navegação para Câmeras
                },
                onOcorrenciasClick = {
                    // Adicionar navegação para Ocorrências
                },
                onFuncionariosClick = {
                    // Adicionar navegação para Funcionários
                },
                onSairClick = {
                    telaAtual = "login"
                }
            )
        }

        // ESQUECEU A SENHA
        "esqueceu" -> {
            EsqueceuSenhaScreen(
                onBackClick = {
                    telaAtual = "login"
                },
                onContinueClick = {
                    telaAtual = "codigo"
                }
            )
        }

        // CÓDIGO DE RECUPERAÇÃO
        "codigo" -> {
            RedefinirSenhaCodigo(
                onBackClick = {
                    telaAtual = "esqueceu"
                },
                onContinueClick = {
                    telaAtual = "trocar"
                }
            )
        }

        // TROCAR SENHA
        "trocar" -> {
            TrocarSenhaScreen(
                onBackClick = {
                    telaAtual = "codigo"
                },
                onRedefinirClick = {
                    telaAtual = "login"
                }
            )
        }
    }
}

@Composable
fun LoginScreen(
    onEntrarClick: () -> Unit,
    onEsqueceuSenhaClick: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // IMAGEM DE FUNDO
        Image(
            painter = painterResource(id = R.drawable.fundo),
            contentDescription = "Fundo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // CONTEÚDO DA TELA
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            // LOGO OROSCORP
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Oroscorp",
                modifier = Modifier
                    .width(180.dp)
                    .height(50.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(80.dp))

            // TÍTULO
            Text(
                text = "ENTRE NA SUA CONTA",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // CAMPO DE E-MAIL
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text("E-mail", color = Color.Gray)
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    unfocusedContainerColor = Color(0xFFF5F3F3),
                    focusedContainerColor = Color(0xFFF5F3F3),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // CAMPO DE SENHA
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = {
                    Text("SENHA", color = Color.Gray)
                },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    unfocusedContainerColor = Color(0xFFF5F3F3),
                    focusedContainerColor = Color(0xFFF5F3F3),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ESQUECEU SUA SENHA
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                TextButton(
                    onClick = onEsqueceuSenhaClick
                ) {
                    Text(
                        text = "Esqueceu sua senha?",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // BOTÃO ENTRAR
            Button(
                onClick = onEntrarClick,
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
        }
    }
}

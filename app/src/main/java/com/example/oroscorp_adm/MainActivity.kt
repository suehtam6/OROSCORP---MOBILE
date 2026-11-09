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
import com.example.oroscorp_adm.view.screens.CamerasScreen
import com.example.oroscorp_adm.view.screens.EsqueceuSenhaScreen
import com.example.oroscorp_adm.view.screens.FuncionariosScreen
import com.example.oroscorp_adm.view.screens.HomeScreen
import com.example.oroscorp_adm.view.screens.OcorrenciasScreen
import com.example.oroscorp_adm.view.screens.RedefinirSenhaCodigo
import com.example.oroscorp_adm.view.screens.RegistrarOcorrenciaScreen
import com.example.oroscorp_adm.view.screens.TrocarSenhaScreen
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
    var telaAtual by remember { mutableStateOf("login") }

    when (telaAtual) {

        "login" -> {
            LoginScreen(
                onEntrarClick = { telaAtual = "home" },
                onEsqueceuSenhaClick = { telaAtual = "esqueceu" }
            )
        }

        "home" -> {
            HomeScreen(
                onDashboardClick = { telaAtual = "home" },
                onSetoresClick = {},
                onCamerasClick = {
                    telaAtual = "cameras"
                },
                onOcorrenciasClick = {
                    telaAtual = "ocorrencias"
                },
                onFuncionariosClick = {
                    telaAtual = "funcionarios"
                },
                onSairClick = {
                    telaAtual = "login"
                }
            )
        }

        "cameras" -> {
            CamerasScreen(
                onBackClick = {
                    telaAtual = "home"
                }
            )
        }

        "ocorrencias" -> {
            OcorrenciasScreen(
                onBackClick = {
                    telaAtual = "home"
                },
                onRegistrarClick = {
                    telaAtual = "registrar_ocorrencia"
                }
            )
        }

        "registrar_ocorrencia" -> {
            RegistrarOcorrenciaScreen(
                onBackClick = {
                    telaAtual = "ocorrencias"
                },
                onRegistrarSucesso = {
                    telaAtual = "ocorrencias"
                }
            )
        }

        "funcionarios" -> {
            FuncionariosScreen(
                onBackClick = {
                    telaAtual = "home"
                }
            )
        }

        "esqueceu" -> {
            EsqueceuSenhaScreen(
                onBackClick = { telaAtual = "login" },
                onContinueClick = { telaAtual = "codigo" }
            )
        }

        "codigo" -> {
            RedefinirSenhaCodigo(
                onBackClick = { telaAtual = "esqueceu" },
                onContinueClick = { telaAtual = "trocar" }
            )
        }

        "trocar" -> {
            TrocarSenhaScreen(
                onBackClick = { telaAtual = "codigo" },
                onRedefinirClick = { telaAtual = "login" }
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

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fundo),
            contentDescription = "Fundo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Oroscorp",
                modifier = Modifier
                    .width(180.dp)
                    .height(50.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(80.dp))

            Text(
                text = "ENTRE NA SUA CONTA",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("e-mail", color = Color.Gray) },
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

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Senha", color = Color.Gray) },
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

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                TextButton(onClick = onEsqueceuSenhaClick) {
                    Text(
                        "Esqueceu sua senha?",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

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
                    "ENTRAR",
                    color = Color(0xFFFF7200),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

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
import com.example.oroscorp_adm.view.screens.EsqueceuSenhaScreen
import com.example.oroscorp_adm.view.screens.HomeScreen
import com.example.oroscorp_adm.view.screens.FuncionariosScreen
import com.example.oroscorp_adm.view.screens.RedefinirSenhaCodigo
import com.example.oroscorp_adm.view.screens.TrocarSenhaScreen
import com.example.oroscorp_adm.ui.theme.OroscorpadmTheme
import com.example.oroscorp_adm.view.screens.LoginScreen

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
                onCamerasClick = {},
                onOcorrenciasClick = {},
                onFuncionariosClick = {
                    telaAtual = "funcionarios"
                },
                onSairClick = {
                    telaAtual = "login"
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


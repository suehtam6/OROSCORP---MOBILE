package com.example.oroscorp_adm.view.screens

import com.example.oroscorp_adm.ui.theme.TemaGlobal
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oroscorp_adm.R

@Composable
fun LoginScreen(
    onEntrarClick: () -> Unit,
    onEsqueceuSenhaClick: () -> Unit,
    onEntrarComCredenciais: ((String, String) -> Unit)? = null,
    carregando: Boolean = false,
    erro: String? = null
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
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
                    .height(50.dp).background(Color.Black, RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(80.dp))

            Text(
                text = "ENTRE NA SUA CONTA",
                color = TemaGlobal.cor(Color.White, Color(0xFF191C1F)),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("E-mail", color = TemaGlobal.cor(Color.Gray, Color(0xFF60656B))) },
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
                    unfocusedBorderColor = TemaGlobal.cor(Color.Transparent, TemaGlobal.borda),
                    focusedBorderColor = TemaGlobal.cor(Color.Transparent, TemaGlobal.laranja)
                )
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("SENHA", color = TemaGlobal.cor(Color.Gray, Color(0xFF60656B))) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    autoCorrectEnabled = false,
                    capitalization = KeyboardCapitalization.None
                ),
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
                    unfocusedBorderColor = TemaGlobal.cor(Color.Transparent, TemaGlobal.borda),
                    focusedBorderColor = TemaGlobal.cor(Color.Transparent, TemaGlobal.laranja)
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
                        color = TemaGlobal.cor(Color.White, Color(0xFF191C1F)),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            if (!erro.isNullOrBlank()) {
                Text(erro, color = TemaGlobal.cor(Color(0xFFFF9E85), Color(0xFFB3261E)), fontSize = 12.sp)
                Spacer(Modifier.height(12.dp))
            }

            Button(
                onClick = {
                    if (onEntrarComCredenciais != null) {
                        onEntrarComCredenciais(email.trim(), password)
                    } else {
                        onEntrarClick()
                    }
                },
                enabled = !carregando && email.isNotBlank() && password.isNotBlank(),
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
                    color = TemaGlobal.cor(Color(0xFFFF7200), Color(0xFFB85A00)),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

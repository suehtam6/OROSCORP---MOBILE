package com.example.oroscorp_adm.components.setorScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SubCardSetores(
    modifier: Modifier = Modifier,
    icone: String,
    quantidade: Int? = null,
    titulo: String
) {
    Card(){
        Column() {
            Text(icone)
            Text("$quantidade")
            Text(titulo)
        }
    }
}
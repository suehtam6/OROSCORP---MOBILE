package com.example.oroscorp_adm.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.oroscorp_adm.components.Header
import com.example.oroscorp_adm.components.setorScreen.CardSetor
import com.example.oroscorp_adm.model.Setor

@Composable
fun SetoresScreen2 (
    setor: Setor,
    modifier: Modifier = Modifier
) {
    val listaSetoresState by remember { mutableStateOf(listOf<Setor>()) }


    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Header()

        if (listaSetoresState.isNotEmpty()) {

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                items(listaSetoresState) {
                    CardSetor(setor = it)
                }
            }
        }
        else{
            Text("404: not found")
        }
    }
}
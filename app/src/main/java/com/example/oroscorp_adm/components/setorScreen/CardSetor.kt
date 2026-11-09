package com.example.oroscorp_adm.components.setorScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oroscorp_adm.R

@Composable
fun CardSetor(
    titulo: String,
    descricao: String,
    funcionarios: Int? = null,
    cameras: Int? = null,
    ocorrenciasAbertas: Int? = null
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(155.dp)
            .background(colorResource(R.color.Cartao), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column() {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column() {
                    Text(titulo)
                    Text(descricao)
                }
            }
        }
        Row() {
            SubCardSetores(
                icone = "",
                quantidade = funcionarios,
                titulo = "Funcionários"
            )
            SubCardSetores(
                icone = "",
                quantidade = cameras,
                titulo = "Câmeras"
            )
            SubCardSetores(
                icone = "",
                quantidade = ocorrenciasAbertas,
                titulo = "Ocorrências abertas"
            )
        }
        Button() {
            Text("Ver setor >")
        }
    }






    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(155.dp)
            .background(colorResource(R.color.Cartao), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        colorResource(R.color.Cartao),
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    simbolo,
                    color = colorResource(R.color.Salmao),
                    fontSize = 24.sp
                )
            }

            if (variacao != null) {
                Text(
                    text = variacao,
                    color = colorResource(R.color.Salmao),
                    fontSize = 12.sp
                )
            }
        }

        Column {
            Text(
                text = valor,
                color = Color(0xFFE1E1E1),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = titulo,
                color = colorResource(R.color.TextoClaro),
                fontSize = 13.sp
            )
        }
    }
}

package com.example.oroscorp_adm.components.setorScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    ocorrenciasAbertas: Int? = null,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(R.color.Cartao))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column {
                Text(
                    text = titulo,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = descricao,
                    color = colorResource(R.color.TextoClaro),
                    fontSize = 13.sp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    SubCardSetores(
                        icone = "",
                        quantidade = funcionarios,
                        titulo = "Funcionários"
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    SubCardSetores(
                        icone = "",
                        quantidade = cameras,
                        titulo = "Câmeras"
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    SubCardSetores(
                        icone = "",
                        quantidade = ocorrenciasAbertas,
                        titulo = "Ocorrências abertas"
                    )
                }
            }

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver setor >")
            }
        }
    }
}

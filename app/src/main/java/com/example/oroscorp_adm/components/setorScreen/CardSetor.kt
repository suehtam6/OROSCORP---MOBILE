
package com.example.oroscorp_adm.components.setorScreen
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.oroscorp_adm.model.Setor

@Composable
fun CardSetor(
    setor: Setor,
    onAbrirSetor: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = setor.nome)
            Text(text = setor.descricao)
            Text(text = setor.local)
            }

        SubCardSetores(
            icone = "",
            quantidade = 0, //
            titulo = "Funcionários"
            )

        SubCardSetores(
            icone = "",
            quantidade = 0, //
            titulo = "Câmeras"
        )

        SubCardSetores(
            icone = "",
            quantidade = 0, //
            titulo = "Abertas"
        )
        Button(
                onClick = onAbrirSetor,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver setor >")
            }
        }
    }

//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(12.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(colorResource(R.color.Cartao))
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            Column {
//                Text(
//                    text = setor.nome,
//                    color = Color.White,
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Text(
//                    text = setor.descricao,
//                    color = colorResource(R.color.TextoClaro),
//                    fontSize = 13.sp
//                )
//            }
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                Box(modifier = Modifier.weight(1f)) {
//                    SubCardSetores(
//                        icone = "",
//                        quantidade = funcionarios.size,
//                        titulo = "Funcionários"
//                    )
//                }
//
//                Box(modifier = Modifier.weight(1f)) {
//                    SubCardSetores(
//                        icone = "",
//                        quantidade = cameras.size, //?????
//                        titulo = "Câmeras"
//                    )
//                }
//
//                Box(modifier = Modifier.weight(1f)) {
//                    SubCardSetores(
//                        icone = "",
//                        quantidade = ocorrencias.size, //???????
//                        titulo = "Ocorrências abertas"
//                    )
//                }
//            }
//
//            Button(
//                onClick = onClick,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Ver setor >")
//            }
//        }
//    }
//}

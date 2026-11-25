
package com.example.oroscorp_adm.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oroscorp_adm.ui.theme.TemaGlobal

@Composable
fun BotaoTema(
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val descricao = if (TemaGlobal.modoClaro) {
        "Ativar modo escuro"
    } else {
        "Ativar modo claro"
    }

    OutlinedButton(

        onClick = {

            TemaGlobal.alternar(context)
        },

        modifier = modifier
            .size(48.dp)
            .semantics {
                contentDescription = descricao
            },

        shape = CircleShape,

        contentPadding = PaddingValues(0.dp),

        border = BorderStroke(
            1.dp,
            TemaGlobal.laranja
        ),

        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = TemaGlobal.cartao,
            contentColor = TemaGlobal.laranja
        )

    ) {

        Text(

            text = if (TemaGlobal.modoClaro) {

                "☾"

            } else {

                "☀"
            },

            fontSize = 25.sp,

            color = TemaGlobal.laranja
        )
    }
}

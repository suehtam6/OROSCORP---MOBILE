
package com.example.oroscorp_adm.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import com.example.oroscorp_adm.R
import com.example.oroscorp_adm.ui.theme.TemaGlobal

@Composable
fun Header() {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp)
            .background(TemaGlobal.cabecalho)
            .padding(horizontal = 12.dp),

        verticalAlignment = Alignment.CenterVertically,

        horizontalArrangement =
            Arrangement.SpaceBetween

    ) {

        Image(

            painter = painterResource(R.drawable.logo),

            contentDescription = "Logo OROSCORP",

            modifier = Modifier
                .weight(1f)
                .height(75.dp),

            contentScale = ContentScale.Fit,

            alignment = Alignment.CenterStart
        )

        Spacer(Modifier.width(10.dp))
    }
}

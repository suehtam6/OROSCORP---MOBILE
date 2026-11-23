package com.example.oroscorp_adm.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.oroscorp_adm.R

@Composable

fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp)
            .background(Color.Black)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
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
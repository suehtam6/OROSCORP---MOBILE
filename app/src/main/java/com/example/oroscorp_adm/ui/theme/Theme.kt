
package com.example.oroscorp_adm.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(

    primary = Color(0xFFFF9500),
    onPrimary = Color.Black,

    secondary = Color(0xFFFFB580),
    onSecondary = Color.Black,

    background = Color(0xFF1C1E20),
    onBackground = Color(0xFFF5F5F5),

    surface = Color(0xFF101315),
    onSurface = Color(0xFFF5F5F5),

    surfaceVariant = Color(0xFF191C1F),
    onSurfaceVariant = Color(0xFF9CA0A6),

    outline = Color(0xFF292D31),

    error = Color(0xFFFF7777),

    onError = Color.Black
)

private val LightColorScheme = lightColorScheme(

    primary = Color(0xFFE87500),
    onPrimary = Color.Black,

    secondary = Color(0xFFB85A00),
    onSecondary = Color.White,

    background = Color(0xFFF2F3F5),
    onBackground = Color(0xFF191C1F),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF191C1F),

    surfaceVariant = Color(0xFFE9EBEE),
    onSurfaceVariant = Color(0xFF60656B),

    outline = Color(0xFFD4D7DB),

    error = Color(0xFFB3261E),

    onError = Color.White
)

@Composable
fun OroscorpadmTheme(
    darkTheme: Boolean = !TemaGlobal.modoClaro,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) {

        DarkColorScheme

    } else {

        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

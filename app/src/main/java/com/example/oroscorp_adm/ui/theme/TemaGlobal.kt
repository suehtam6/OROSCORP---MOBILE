
package com.example.oroscorp_adm.ui.theme

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

object TemaGlobal {

    var modoClaro by mutableStateOf(false)
        private set

    fun cor(
        escura: Color,
        clara: Color
    ): Color {
        return if (modoClaro) clara else escura
    }

    fun carregar(context: Context) {

        val preferencias = context.getSharedPreferences(
            "oroscorp_tema",
            Context.MODE_PRIVATE
        )

        modoClaro = preferencias.getBoolean(
            "modo_claro",
            false
        )
    }

    fun alternar(context: Context) {

        modoClaro = !modoClaro

        context.getSharedPreferences(
            "oroscorp_tema",
            Context.MODE_PRIVATE
        )
            .edit()
            .putBoolean("modo_claro", modoClaro)
            .apply()
    }

    val fundo: Color
        get() = cor(
            Color(0xFF1C1E20),
            Color(0xFFF2F3F5)
        )

    val cabecalho: Color
        get() = cor(
            Color(0xFF080A0B),
            Color.White
        )

    val cartao: Color
        get() = cor(
            Color(0xFF101315),
            Color.White
        )

    val cartaoSecundario: Color
        get() = cor(
            Color(0xFF191C1F),
            Color(0xFFE9EBEE)
        )

    val texto: Color
        get() = cor(
            Color(0xFFF5F5F5),
            Color(0xFF191C1F)
        )

    val textoSecundario: Color
        get() = cor(
            Color(0xFF9CA0A6),
            Color(0xFF60656B)
        )

    val borda: Color
        get() = cor(
            Color(0xFF292D31),
            Color(0xFFD4D7DB)
        )

    val laranja: Color
        get() = cor(
            Color(0xFFFF9500),
            Color(0xFFE87500)
        )

    val verde: Color
        get() = cor(
            Color(0xFF20C982),
            Color(0xFF00864A)
        )

    val vermelho: Color
        get() = cor(
            Color(0xFFFF7777),
            Color(0xFFB3261E)
        )

    val salmao: Color
        get() = cor(
            Color(0xFFFF9966),
            Color(0xFFAD501B)
        )

    val textoBotao: Color
        get() = Color.Black

    val fundoVideo: Color
        get() = Color.Black
}

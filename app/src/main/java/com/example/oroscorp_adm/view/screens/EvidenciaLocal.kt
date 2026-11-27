package com.example.oroscorp_adm.view.screens

import android.content.Context
import android.net.Uri
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle
import java.util.Locale

object EvidenciaLocal {
    private const val MAX_BYTES = 5 * 1024 * 1024

    suspend fun lerBase64(context: Context, endereco: String): String = withContext(Dispatchers.IO) {
        val uri = Uri.parse(endereco)
        require(uri.scheme == "content" || uri.scheme == "file") { "Imagem local inválida." }
        val bytes = context.contentResolver.openInputStream(uri)?.use { entrada ->
            val saida = ByteArrayOutputStream()
            val buffer = ByteArray(8192)
            var total = 0
            while (true) {
                val n = entrada.read(buffer)
                if (n < 0) break
                total += n
                require(total <= MAX_BYTES) { "Selecione uma imagem de até 5 MB." }
                saida.write(buffer, 0, n)
            }
            saida.toByteArray()
        } ?: error("Não foi possível abrir a imagem selecionada.")
        require(bytes.isNotEmpty()) { "A imagem está vazia." }
        Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    fun dataHoraSql(data: String, horario: String): String {
        val entrada = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm", Locale.ROOT)
            .withResolverStyle(ResolverStyle.STRICT)
        return LocalDateTime.parse("$data $horario", entrada)
            .format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss", Locale.ROOT))
    }
}

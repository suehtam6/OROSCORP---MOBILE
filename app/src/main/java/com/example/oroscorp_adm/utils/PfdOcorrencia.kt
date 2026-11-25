
package com.example.oroscorp_adm.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument

import com.example.oroscorp_adm.R
import com.example.oroscorp_adm.view.screens.DetalhesOcorrencia

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import okhttp3.OkHttpClient
import okhttp3.Request

import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

object PdfOcorrencia {

    private const val LARGURA = 595
    private const val ALTURA = 842

    private const val MARGEM = 38f
    private const val LIMITE_INFERIOR = 780f

    private val LARANJA =
        Color.rgb(255, 102, 0)

    private val ESCURO =
        Color.rgb(19, 22, 25)

    private val CINZA =
        Color.rgb(100, 105, 110)

    private val CINZA_CLARO =
        Color.rgb(230, 232, 235)

    private val cliente = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(40, TimeUnit.SECONDS)
        .callTimeout(60, TimeUnit.SECONDS)
        .build()


    suspend fun gerar(
        context: Context,
        ocorrencia: DetalhesOcorrencia

    ): File = withContext(Dispatchers.IO) {

        val pasta = File(
            context.cacheDir,
            "relatorios"
        )

        if (!pasta.exists() && !pasta.mkdirs()) {
            error("Não foi possível criar a pasta de relatórios.")
        }

        val arquivo = File(
            pasta,
            "ocorrencia_${ocorrencia.id}_${System.currentTimeMillis()}.pdf"
        )

        val logo = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.logo
        )

        val evidencia = if (
            !ocorrencia.evidenciaUrl.isNullOrBlank()
        ) {
            baixarImagem(ocorrencia.evidenciaUrl)
        } else {
            null
        }

        val documento = PdfDocument()

        var numeroPagina = 0
        var pagina: PdfDocument.Page? = null

        var posicaoY = 0f

        val paint = Paint(
            Paint.ANTI_ALIAS_FLAG
        )


        fun finalizarPagina() {

            val paginaAtual = pagina ?: return

            val canvas = paginaAtual.canvas

            paint.color = LARANJA
            paint.strokeWidth = 2f

            canvas.drawLine(
                MARGEM,
                793f,
                LARGURA - MARGEM,
                793f,
                paint
            )

            paint.color = CINZA
            paint.textSize = 9f
            paint.typeface = Typeface.DEFAULT

            canvas.drawText(
                "OROSCORP - Sistema de monitoramento de segurança",
                MARGEM,
                811f,
                paint
            )

            paint.textAlign = Paint.Align.RIGHT

            canvas.drawText(
                "Página $numeroPagina",
                LARGURA - MARGEM,
                811f,
                paint
            )

            paint.textAlign = Paint.Align.LEFT

            documento.finishPage(paginaAtual)

            pagina = null
        }


        fun novaPagina() {

            finalizarPagina()

            numeroPagina++

            val informacoes = PdfDocument.PageInfo.Builder(
                LARGURA,
                ALTURA,
                numeroPagina
            ).create()

            val novaPagina = documento.startPage(
                informacoes
            )

            pagina = novaPagina

            val canvas = novaPagina.canvas

            canvas.drawColor(Color.WHITE)

            paint.color = ESCURO

            canvas.drawRect(
                0f,
                0f,
                LARGURA.toFloat(),
                90f,
                paint
            )

            if (logo != null) {

                desenharImagemProporcional(
                    canvas = canvas,
                    bitmap = logo,
                    area = RectF(
                        MARGEM,
                        12f,
                        200f,
                        65f
                    )
                )

            } else {

                paint.color = LARANJA
                paint.textSize = 22f
                paint.typeface =
                    Typeface.DEFAULT_BOLD

                canvas.drawText(
                    "OROSCORP",
                    MARGEM,
                    45f,
                    paint
                )
            }

            paint.color = Color.WHITE
            paint.textSize = 13f
            paint.typeface =
                Typeface.DEFAULT_BOLD

            paint.textAlign = Paint.Align.RIGHT

            canvas.drawText(
                "RELATÓRIO DE OCORRÊNCIA",
                LARGURA - MARGEM,
                35f,
                paint
            )

            paint.color = LARANJA
            paint.textSize = 17f

            canvas.drawText(
                "#${ocorrencia.id.toString().padStart(5, '0')}",
                LARGURA - MARGEM,
                60f,
                paint
            )

            paint.textAlign = Paint.Align.LEFT

            paint.color = LARANJA

            canvas.drawRect(
                0f,
                87f,
                LARGURA.toFloat(),
                91f,
                paint
            )

            posicaoY = 125f
        }


        fun verificarEspaco(
            alturaNecessaria: Float
        ) {
            if (
                posicaoY + alturaNecessaria >
                LIMITE_INFERIOR
            ) {
                novaPagina()
            }
        }


        fun titulo(
            texto: String
        ) {

            verificarEspaco(55f)

            val canvas = pagina!!.canvas

            paint.color = LARANJA
            paint.textSize = 14f
            paint.typeface =
                Typeface.DEFAULT_BOLD

            canvas.drawText(
                texto,
                MARGEM,
                posicaoY,
                paint
            )

            posicaoY += 12f

            paint.color = CINZA_CLARO
            paint.strokeWidth = 1f

            canvas.drawLine(
                MARGEM,
                posicaoY,
                LARGURA - MARGEM,
                posicaoY,
                paint
            )

            posicaoY += 22f
        }


        fun texto(
            conteudo: String,
            negrito: Boolean = false
        ) {

            paint.color = ESCURO
            paint.textSize = 11f

            paint.typeface = if (negrito) {
                Typeface.DEFAULT_BOLD
            } else {
                Typeface.DEFAULT
            }

            val linhas = quebrarTexto(
                texto = conteudo.ifBlank {
                    "Não informado"
                },
                paint = paint,
                larguraMaxima =
                    LARGURA - MARGEM * 2
            )

            linhas.forEach { linha ->

                verificarEspaco(18f)

                paint.color = ESCURO
                paint.textSize = 11f

                paint.typeface = if (negrito) {
                    Typeface.DEFAULT_BOLD
                } else {
                    Typeface.DEFAULT
                }

                pagina!!.canvas.drawText(
                    linha,
                    MARGEM,
                    posicaoY,
                    paint
                )

                posicaoY += 17f
            }

            posicaoY += 7f
        }


        fun campo(
            rotulo: String,
            valor: String?
        ) {

            texto(
                "$rotulo: ${valor?.takeIf { it.isNotBlank() } ?: "Não informado"}"
            )
        }


        try {

            novaPagina()

            titulo("INFORMAÇÕES DA OCORRÊNCIA")

            campo(
                "Identificação",
                "#${ocorrencia.id}"
            )

            campo(
                "Status",
                ocorrencia.status
            )

            campo(
                "Prioridade",
                ocorrencia.prioridade
            )

            campo(
                "Origem",
                when {
                    !ocorrencia.origemConhecida ->
                        "Não informada"

                    ocorrencia.tipoIA ->
                        "Inteligência Artificial"

                    else ->
                        "Manual"
                }
            )

            campo(
                "Tipo",
                ocorrencia.tipo
            )

            campo(
                "Setor",
                ocorrencia.setor
            )

            campo(
                "Câmera",
                ocorrencia.camera
            )

            campo(
                "Data",
                ocorrencia.data
            )

            campo(
                "Horário",
                ocorrencia.horario
            )

            posicaoY += 12f

            titulo("FUNCIONÁRIO")

            campo(
                "Nome",
                ocorrencia.funcionario
                    ?: "Não identificado"
            )

            campo(
                "Matrícula",
                ocorrencia.matricula
            )

            campo(
                "Cargo",
                ocorrencia.cargo
            )

            posicaoY += 12f

            titulo("EQUIPAMENTOS DE PROTEÇÃO")

            campo(
                "EPIs",
                ocorrencia.epi
            )

            if (
                ocorrencia.origemConhecida &&
                ocorrencia.tipoIA
            ) {

                campo(
                    "Confiança da IA",
                    ocorrencia.confiancaIA
                        ?.let { "$it" }
                )

                campo(
                    "Detecção",
                    ocorrencia.deteccaoIA
                )
            }

            posicaoY += 12f

            titulo("DESCRIÇÃO")

            texto(
                ocorrencia.descricao
            )

            posicaoY += 12f

            if (evidencia != null) {

                verificarEspaco(335f)

                titulo("EVIDÊNCIA FOTOGRÁFICA")

                desenharImagemProporcional(
                    canvas = pagina!!.canvas,
                    bitmap = evidencia,
                    area = RectF(
                        MARGEM,
                        posicaoY,
                        LARGURA - MARGEM,
                        posicaoY + 280f
                    )
                )

                posicaoY += 300f

            } else {

                titulo("EVIDÊNCIA FOTOGRÁFICA")

                texto(
                    "Nenhuma evidência fotográfica informada."
                )
            }

            titulo("HISTÓRICO")

            if (ocorrencia.historico.isEmpty()) {

                texto(
                    "Nenhum registro de histórico disponibilizado."
                )

            } else {

                ocorrencia.historico.forEach {

                    campo(
                        "Horário",
                        it.horario
                    )

                    texto(
                        it.descricao
                    )

                    posicaoY += 8f
                }
            }

            finalizarPagina()

            FileOutputStream(arquivo).use {

                documento.writeTo(it)
            }

            if (
                !arquivo.exists() ||
                arquivo.length() == 0L
            ) {
                error("O PDF gerado está vazio.")
            }

            arquivo

        } catch (e: Exception) {

            arquivo.delete()

            throw e

        } finally {

            finalizarPagina()

            documento.close()

            logo?.recycle()
            evidencia?.recycle()
        }
    }


    private fun baixarImagem(
        url: String
    ): Bitmap {

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        cliente.newCall(request)
            .execute()
            .use { response ->

                if (!response.isSuccessful) {

                    error(
                        "Não foi possível carregar a evidência. HTTP ${response.code}"
                    )
                }

                val body = response.body
                    ?: error(
                        "A imagem da evidência está vazia."
                    )

                val limite = 15L * 1024 * 1024

                if (body.contentLength() > limite) {
                    error("A evidência excede o tamanho permitido.")
                }

                val source = body.source()

                if (source.request(limite + 1)) {
                    error("A evidência excede o tamanho permitido.")
                }

                val bytes = source.readByteArray()

                val bounds = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }

                BitmapFactory.decodeByteArray(
                    bytes,
                    0,
                    bytes.size,
                    bounds
                )

                if (
                    bounds.outWidth <= 0 ||
                    bounds.outHeight <= 0
                ) {
                    error("Formato de imagem inválido.")
                }

                var amostragem = 1

                while (
                    bounds.outWidth / amostragem > 1800 ||
                    bounds.outHeight / amostragem > 1800
                ) {
                    amostragem *= 2
                }

                val options = BitmapFactory.Options().apply {
                    inSampleSize = amostragem
                }

                return BitmapFactory.decodeByteArray(
                    bytes,
                    0,
                    bytes.size,
                    options
                ) ?: error(
                    "Não foi possível processar a imagem."
                )
            }
    }


    private fun desenharImagemProporcional(
        canvas: Canvas,
        bitmap: Bitmap,
        area: RectF
    ) {

        val escala = minOf(
            area.width() / bitmap.width,
            area.height() / bitmap.height
        )

        val largura =
            bitmap.width * escala

        val altura =
            bitmap.height * escala

        val esquerda =
            area.left +
                    (area.width() - largura) / 2

        val topo =
            area.top +
                    (area.height() - altura) / 2

        val destino = RectF(
            esquerda,
            topo,
            esquerda + largura,
            topo + altura
        )

        canvas.drawBitmap(
            bitmap,
            null,
            destino,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )
    }


    private fun quebrarTexto(
        texto: String,
        paint: Paint,
        larguraMaxima: Float

    ): List<String> {

        val linhas = mutableListOf<String>()

        texto.split("\n").forEach { paragrafo ->

            if (paragrafo.isBlank()) {
                linhas.add("")
            } else {

                var restante = paragrafo

                while (restante.isNotEmpty()) {

                    var quantidade = paint.breakText(
                        restante,
                        true,
                        larguraMaxima,
                        null
                    ).coerceAtLeast(1)

                    if (quantidade < restante.length) {

                        val ultimoEspaco =
                            restante.lastIndexOf(
                                ' ',
                                quantidade - 1
                            )

                        if (ultimoEspaco > 0) {
                            quantidade = ultimoEspaco
                        }
                    }

                    linhas.add(
                        restante.substring(
                            0,
                            quantidade
                        ).trim()
                    )

                    restante = restante
                        .substring(quantidade)
                        .trimStart()
                }
            }
        }

        return linhas
    }
}

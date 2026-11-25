
package com.example.oroscorp_adm.view.screens

import com.example.oroscorp_adm.ui.theme.TemaGlobal
import android.content.Intent
import android.net.Uri

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.oroscorp_adm.model.RelatorioPdf
import com.example.oroscorp_adm.repository.RelatorioPdfRepository
import com.example.oroscorp_adm.utils.PdfOcorrencia

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

import java.io.File

private val FundoPdf
    get() = TemaGlobal.cor(Color(0xFF101315), Color(0xFFFFFFFF))

private val LaranjaPdf
    get() = TemaGlobal.cor(Color(0xFFFF9500), Color(0xFFB85A00))

private val BrancoPdf
    get() = TemaGlobal.cor(Color(0xFFF5F5F5), Color(0xFF191C1F))

private val CinzaPdf
    get() = TemaGlobal.cor(Color(0xFF9CA0A6), Color(0xFF60656B))


@Composable
fun ExportarPdfScreen(
    ocorrencia: DetalhesOcorrencia?,

    onBackClick: () -> Unit,

    onAdicionarFuncionario: () -> Unit = {},

    onAlterarStatus: () -> Unit = {}
) {

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val repository = remember {
        RelatorioPdfRepository()
    }

    var carregando by remember {
        mutableStateOf(false)
    }

    var etapa by remember {
        mutableStateOf("")
    }

    var erro by remember {
        mutableStateOf<String?>(null)
    }

    var relatorio by remember {
        mutableStateOf<RelatorioPdf?>(null)
    }


    fun exportar() {

        if (carregando) {
            return
        }

        val ocorrenciaAtual = ocorrencia

        if (ocorrenciaAtual == null) {

            erro = "Nenhuma ocorrência foi selecionada."

            return
        }

        carregando = true
        erro = null
        relatorio = null

        scope.launch {

            var arquivo: File? = null

            try {

                etapa = "Gerando relatório PDF..."

                val pdf = PdfOcorrencia.gerar(
                    context = context,
                    ocorrencia = ocorrenciaAtual
                )

                arquivo = pdf

                etapa =
                    "Armazenando relatório no Cloudinary..."

                relatorio = repository.enviarPdf(
                    idOcorrencia = ocorrenciaAtual.id,
                    arquivo = pdf
                )

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                erro = when (e) {

                    is java.net.SocketTimeoutException ->
                        "O servidor demorou demais para responder."

                    is java.net.UnknownHostException ->
                        "Não foi possível localizar o servidor."

                    is java.net.ConnectException ->
                        "Não foi possível conectar ao servidor."

                    else ->
                        e.message
                            ?: "Falha ao exportar o relatório."
                }

            } finally {

                arquivo?.delete()

                carregando = false
                etapa = ""
            }
        }
    }


    fun abrirPdf(
        dados: RelatorioPdf
    ) {

        scope.launch {

            carregando = true
            etapa = "Consultando relatório..."

            try {

                val atualizado =
                    repository.buscarRelatorio(
                        dados.idOcorrencia
                    )

                val url = Uri.parse(
                    atualizado.secureUrl
                )

                require(url.scheme == "https") {
                    "O servidor retornou uma URL inválida."
                }

                val intent = Intent(
                    Intent.ACTION_VIEW,
                    url
                )

                context.startActivity(intent)

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                relatorio = null

                erro = e.message
                    ?: "Não foi possível abrir o PDF."

            } finally {

                carregando = false
                etapa = ""
            }
        }
    }


    DetalhesOcorrenciaScreen(
        ocorrencia = ocorrencia,

        onBackClick = {
            if (!carregando) {
                onBackClick()
            }
        },

        onAdicionarFuncionario =
            onAdicionarFuncionario,

        onAlterarStatus =
            onAlterarStatus,

        onExportarPDF = {
            exportar()
        }
    )


    if (carregando) {

        AlertDialog(
            onDismissRequest = {},

            containerColor = FundoPdf,

            shape = RoundedCornerShape(16.dp),

            title = {

                Text(
                    text = "Exportando relatório",
                    color = LaranjaPdf,
                    fontWeight = FontWeight.Bold
                )
            },

            text = {

                Column(
                    modifier = Modifier.fillMaxWidth(),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    CircularProgressIndicator(
                        color = LaranjaPdf
                    )

                    Spacer(
                        Modifier.height(20.dp)
                    )

                    Text(
                        text = etapa,
                        color = BrancoPdf,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        Modifier.height(8.dp)
                    )

                    Text(
                        text = "Aguarde a conclusão da operação.",
                        color = CinzaPdf,
                        fontSize = 12.sp
                    )
                }
            },

            confirmButton = {},

            modifier = Modifier.border(
                1.dp,
                LaranjaPdf,
                RoundedCornerShape(16.dp)
            )
        )
    }


    if (
        relatorio != null &&
        !carregando &&
        erro == null
    ) {

        val resultado = relatorio!!

        AlertDialog(
            onDismissRequest = {
                relatorio = null
            },

            containerColor = FundoPdf,

            shape = RoundedCornerShape(16.dp),

            icon = {

                Text(
                    text = "✓",
                    color = LaranjaPdf,
                    fontSize = 36.sp
                )
            },

            title = {

                Text(
                    text = "Relatório exportado",
                    color = LaranjaPdf,
                    fontWeight = FontWeight.Bold
                )
            },

            text = {

                Column {

                    Text(
                        text = "O relatório foi gerado e armazenado com sucesso no Cloudinary.",
                        color = BrancoPdf
                    )

                    Spacer(
                        Modifier.height(14.dp)
                    )

                    Text(
                        text = "Ocorrência #${resultado.idOcorrencia}",
                        color = CinzaPdf
                    )

                    Text(
                        text = "Relatório nº ${resultado.id}",
                        color = CinzaPdf
                    )
                }
            },

            confirmButton = {

                Button(
                    onClick = {
                        abrirPdf(resultado)
                    },

                    colors = ButtonDefaults.buttonColors(
                        containerColor = LaranjaPdf,
                        contentColor = Color.Black
                    ),

                    shape = RoundedCornerShape(10.dp)
                ) {

                    Text(
                        text = "Abrir PDF",
                        fontWeight = FontWeight.Bold
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        relatorio = null
                    }
                ) {

                    Text(
                        text = "Fechar",
                        color = LaranjaPdf
                    )
                }
            },

            modifier = Modifier.border(
                1.dp,
                LaranjaPdf,
                RoundedCornerShape(16.dp)
            )
        )
    }


    if (!erro.isNullOrBlank()) {

        AlertDialog(
            onDismissRequest = {
                erro = null
            },

            containerColor = FundoPdf,

            shape = RoundedCornerShape(16.dp),

            icon = {

                Text(
                    text = "⚠",
                    color = LaranjaPdf,
                    fontSize = 34.sp
                )
            },

            title = {

                Text(
                    text = "Ocorreu um erro",
                    color = LaranjaPdf,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
            },

            text = {

                Column {

                    Text(
                        text = "Não foi possível exportar o relatório.",
                        color = BrancoPdf
                    )

                    Spacer(
                        Modifier.height(12.dp)
                    )

                    Text(
                        text = erro.orEmpty(),
                        color = CinzaPdf,
                        lineHeight = 20.sp
                    )
                }
            },

            confirmButton = {

                Button(
                    onClick = {
                        erro = null
                    },

                    colors = ButtonDefaults.buttonColors(
                        containerColor = LaranjaPdf,
                        contentColor = Color.Black
                    ),

                    shape = RoundedCornerShape(10.dp)
                ) {

                    Text(
                        text = "Entendi",
                        fontWeight = FontWeight.Bold
                    )
                }
            },

            modifier = Modifier.border(
                1.dp,
                LaranjaPdf,
                RoundedCornerShape(16.dp)
            )
        )
    }
}

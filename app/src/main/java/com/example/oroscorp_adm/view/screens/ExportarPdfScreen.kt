
package com.example.oroscorp_adm.view.screens

import com.example.oroscorp_adm.ui.theme.TemaGlobal
import android.content.Intent
import android.content.ClipData
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.saveable.rememberSaveable
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

import com.example.oroscorp_adm.utils.PdfOcorrencia

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    var carregando by remember {
        mutableStateOf(false)
    }

    var etapa by remember {
        mutableStateOf("")
    }

    var erro by remember {
        mutableStateOf<String?>(null)
    }

    var caminhoPendente by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var relatorio by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    val salvarPdf = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { destino ->
        val caminho = caminhoPendente
        caminhoPendente = null
        val arquivo = caminho?.let { File(it) }

        if (destino == null) {
            arquivo?.delete()
            carregando = false
            etapa = ""
        } else if (arquivo == null || !arquivo.isFile) {
            erro = "O PDF temporário não foi encontrado. Gere o relatório novamente."
            carregando = false
            etapa = ""
        } else {
            carregando = true
            etapa = "Salvando relatório PDF..."
            scope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        val saida = context.contentResolver.openOutputStream(destino, "w")
                            ?: error("Não foi possível abrir o destino escolhido.")
                        saida.use { output ->
                            arquivo.inputStream().use { input -> input.copyTo(output) }
                        }
                    }
                    // O provedor pode não oferecer permissão persistente.
                    runCatching {
                        context.contentResolver.takePersistableUriPermission(
                            destino, Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    }
                    relatorio = destino.toString()
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    erro = e.message ?: "Não foi possível salvar o PDF."
                } finally {
                    arquivo.delete()
                    carregando = false
                    etapa = ""
                }
            }
        }
    }

    fun exportar() {
        if (carregando || caminhoPendente != null) return
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
                val pdf = PdfOcorrencia.gerar(context.applicationContext, ocorrenciaAtual)
                arquivo = pdf
                caminhoPendente = pdf.absolutePath
                carregando = false
                etapa = ""
                salvarPdf.launch(pdf.name)
                arquivo = null // O callback do seletor fará a limpeza.
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                caminhoPendente = null
                erro = when (e) {
                    is java.net.SocketTimeoutException ->
                        "A evidência demorou demais para carregar."
                    is java.net.UnknownHostException ->
                        "Não foi possível localizar o servidor da evidência."
                    is java.net.ConnectException ->
                        "Não foi possível conectar ao servidor da evidência."
                    else -> e.message ?: "Falha ao exportar o relatório."
                }
            } finally {
                arquivo?.delete()
                carregando = false
                etapa = ""
            }
        }
    }

    fun abrirPdf(dados: String, compartilhar: Boolean = false) {
        try {
            val uri = Uri.parse(dados)
            val intent = if (compartilhar) {
                Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                }
            } else {
                Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/pdf")
                }
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.clipData = ClipData.newRawUri("Relatório PDF", uri)
            context.startActivity(Intent.createChooser(intent, if (compartilhar) "Compartilhar PDF" else "Abrir PDF"))
        } catch (e: Exception) {
            erro = e.message ?: "Não foi possível abrir o PDF."
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
                        text = "O relatório foi gerado e salvo no local escolhido.",
                        color = BrancoPdf
                    )

                    Spacer(
                        Modifier.height(14.dp)
                    )

                    Text(
                        text = "Ocorrência #${ocorrencia?.id ?: "—"}",
                        color = CinzaPdf
                    )

                    TextButton(onClick = { abrirPdf(resultado, compartilhar = true) }) {
                        Text("Compartilhar PDF", color = LaranjaPdf)
                    }
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
                        text = "Não foi possível concluir a operação do PDF.",
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

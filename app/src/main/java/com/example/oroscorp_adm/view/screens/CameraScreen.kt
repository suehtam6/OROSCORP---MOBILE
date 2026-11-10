
package com.example.oroscorp_adm.view.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun CameraScreen(
    nomeCamera: String,
    videoUrl: String? = null,
    online: Boolean = false,
    onBackClick: () -> Unit,
    onAnteriorClick: () -> Unit = {},
    onProximaClick: () -> Unit = {},
    temAnterior: Boolean = false,
    temProxima: Boolean = false
) {
    val laranja = Color(0xFFFF8800)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF333333))
            .verticalScroll(rememberScrollState())
            .padding(14.dp)
    ) {
        Text(
            text = nomeCamera,
            color = Color.White,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(14.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(Color.Black),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .fillMaxHeight()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "O",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            ControleCamera(
                simbolo = "❮",
                ativo = temAnterior,
                onClick = onAnteriorClick
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = 8.dp)
                    .border(
                        2.dp,
                        laranja,
                        RoundedCornerShape(5.dp)
                    )
                    .background(Color(0xFF202020)),
                contentAlignment = Alignment.Center
            ) {
                if (online && !videoUrl.isNullOrBlank()) {
                    VideoCamera(videoUrl)
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            if (online) "AGUARDANDO VÍDEO"
                            else "SEM SINAL",
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            if (online)
                                "Aguardando URL da API"
                            else
                                "Câmera offline",
                            color = Color.Gray,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            ControleCamera(
                simbolo = "❯",
                ativo = temProxima,
                onClick = onProximaClick
            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = if (online)
                "Câmera online"
            else
                "Câmera offline",
            color = if (online)
                laranja else Color.Gray,
            fontSize = 13.sp
        )

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, laranja)
        ) {
            Text(
                "Voltar para câmeras",
                color = laranja
            )
        }
    }
}

@Composable
private fun ControleCamera(
    simbolo: String,
    ativo: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 3.dp)
            .size(30.dp)
            .background(
                Color.Black,
                RoundedCornerShape(4.dp)
            )
            .clickable(enabled = ativo) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            simbolo,
            color = if (ativo) Color.White else Color.DarkGray,
            fontSize = 20.sp
        )
    }
}

@Composable
private fun VideoCamera(url: String) {
    var erro by remember(url) { mutableStateOf(false) }

    if (erro) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Não foi possível carregar a transmissão",
                color = Color.LightGray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    } else {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun onReceivedError(
                            view: WebView?,
                            request: android.webkit.WebResourceRequest?,
                            error: android.webkit.WebResourceError?
                        ) {
                            if (request?.isForMainFrame == true) {
                                erro = true
                            }
                        }
                    }
                    settings.javaScriptEnabled = false
                    loadUrl(url)
                }
            },
            update = { webView ->
                if (webView.url != url) {
                    webView.loadUrl(url)
                }
            },
            onRelease = { webView ->
                webView.stopLoading()
                webView.destroy()
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

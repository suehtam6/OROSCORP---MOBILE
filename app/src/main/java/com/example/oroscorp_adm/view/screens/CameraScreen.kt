
package com.example.oroscorp_adm.view.screens

import com.example.oroscorp_adm.ui.theme.TemaGlobal
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
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun CameraScreen(
    nomeCamera: String,
    videoUrl: String? = null,
    online: Boolean = false,
    statusConhecido: Boolean = true,
    onBackClick: () -> Unit,
    onAnteriorClick: () -> Unit = {},
    onProximaClick: () -> Unit = {},
    temAnterior: Boolean = false,
    temProxima: Boolean = false
) {
    val laranja = TemaGlobal.cor(Color(0xFFFF8800), Color(0xFFB85A00))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TemaGlobal.cor(Color(0xFF333333), Color(0xFFF2F3F5)))
            .verticalScroll(rememberScrollState())
            .padding(14.dp)
    ) {
        Text(
            text = nomeCamera,
            color = TemaGlobal.cor(Color.White, Color(0xFF191C1F)),
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
                            else if (statusConhecido) "SEM SINAL" else "STATUS NÃO INFORMADO",
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
                                if (statusConhecido) "Câmera offline" else "Status não recebido da API",
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
            text = if (online) "Câmera online"
            else if (statusConhecido) "Câmera offline"
            else "Status não disponível",
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
    val recurso = url.substringBefore('?').lowercase()
    val usarMedia3 = url.startsWith("rtsp://", ignoreCase = true) ||
        recurso.endsWith(".m3u8") || recurso.endsWith(".mp4") ||
        recurso.endsWith(".m4v") || recurso.endsWith(".mpd")
    if (usarMedia3) PlayerCameraMedia3(url) else PlayerCameraWeb(url)
}

/** ExoPlayer permite RTSP, HLS, MP4 e DASH, quando os módulos estão no Gradle. */
@Composable
private fun PlayerCameraMedia3(url: String) {
    val context = LocalContext.current
    var erro by remember(url) { mutableStateOf<String?>(null) }
    val player = remember(url) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            playWhenReady = true
            prepare()
        }
    }
    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                erro = "Falha ao reproduzir: ${error.errorCodeName}"
            }
        }
        player.addListener(listener)
        onDispose {
            player.removeListener(listener)
            player.release()
        }
    }
    if (erro == null) {
        AndroidView(
            factory = { ctx -> PlayerView(ctx).apply { useController = false; this.player = player } },
            update = { it.player = player },
            modifier = Modifier.fillMaxSize()
        )
    } else {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(erro.orEmpty(), color = Color.LightGray, fontSize = 12.sp, textAlign = TextAlign.Center)
        }
    }
}

/** WebView apenas para streams HTTP compatíveis (ex.: MJPEG/HTML); não suporta RTSP. */
@Composable
private fun PlayerCameraWeb(url: String) {
    var erro by remember(url) { mutableStateOf(false) }
    if (!url.startsWith("http://", true) && !url.startsWith("https://", true)) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Protocolo de vídeo não suportado", color = Color.LightGray, fontSize = 12.sp)
        }
        return
    }
    if (erro) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Não foi possível carregar a transmissão", color = Color.LightGray,
                fontSize = 12.sp, textAlign = TextAlign.Center)
        }
    } else {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun onReceivedError(
                            view: WebView?, request: android.webkit.WebResourceRequest?,
                            error: android.webkit.WebResourceError?
                        ) {
                            if (request?.isForMainFrame == true) erro = true
                        }
                    }
                    settings.javaScriptEnabled = false
                    loadUrl(url)
                }
            },
            update = { webView -> if (webView.url != url) webView.loadUrl(url) },
            onRelease = { webView -> webView.stopLoading(); webView.destroy() },
            modifier = Modifier.fillMaxSize()
        )
    }
}

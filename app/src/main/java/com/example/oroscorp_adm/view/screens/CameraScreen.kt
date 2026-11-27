
package com.example.oroscorp_adm.view.screens

import com.example.oroscorp_adm.ui.theme.TemaGlobal
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.rtsp.RtspMediaSource
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import java.net.HttpURLConnection
import java.net.URL
import java.io.ByteArrayOutputStream
import java.io.EOFException

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
                if (!videoUrl.isNullOrBlank()) {
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
            text = if (online) "Status cadastrado: online"
            else if (statusConhecido) "Status cadastrado: offline"
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
    val endereco = url.trim()
    val caminho = endereco.substringBefore('?').substringBefore('#').lowercase()
    val rtsp = endereco.startsWith("rtsp://", true)
    val http = endereco.startsWith("http://", true) || endereco.startsWith("https://", true)
    val video = rtsp || (http && listOf(".m3u8", ".mp4", ".m4v", ".mpd", ".webm", ".ogg").any { caminho.endsWith(it) })
    val owner = LocalLifecycleOwner.current
    var ativo by remember(owner) {
        mutableStateOf(owner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
    }
    DisposableEffect(owner) {
        val observer = LifecycleEventObserver { _, _ ->
            ativo = owner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
        }
        owner.lifecycle.addObserver(observer)
        onDispose { owner.lifecycle.removeObserver(observer) }
    }
    var tentativa by remember(endereco) { mutableIntStateOf(0) }
    var mensagem by remember(endereco, tentativa, ativo) { mutableStateOf("Conectando à câmera...") }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (ativo) {
            key(endereco, tentativa) {
                when {
                    video -> PlayerCameraMedia3(endereco) { mensagem = it }
                    http -> PlayerCameraMjpeg(endereco) { mensagem = it }
                    else -> LaunchedEffect(endereco) { mensagem = "Protocolo não suportado" }
                }
            }
        }
        if (ativo && mensagem.isEmpty()) {
            Text("VÍDEO RECEBIDO", color = Color.White, fontSize = 10.sp,
                modifier = Modifier.align(Alignment.TopStart).background(Color.Black.copy(alpha = 0.7f)).padding(5.dp))
        }
        if (mensagem.isNotEmpty()) {
            Column(Modifier.align(Alignment.Center).background(Color.Black.copy(alpha = 0.8f)).padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (ativo) mensagem else "Transmissão pausada", color = Color.White, fontSize = 12.sp, textAlign = TextAlign.Center)
                if (ativo) TextButton(onClick = { tentativa++ }) { Text("Reconectar", color = Color(0xFFFF8800)) }
            }
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun PlayerCameraMedia3(url: String, onStatus: (String) -> Unit) {
    val context = LocalContext.current
    val status by rememberUpdatedState(onStatus)
    val player = remember(url, context) { ExoPlayer.Builder(context).build() }
    DisposableEffect(player, url) {
        var quadroRecebido = false
        val listener = object : Player.Listener {
            override fun onRenderedFirstFrame() {
                quadroRecebido = true
                status("")
            }
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> status("Carregando transmissão...")
                    Player.STATE_READY -> if (quadroRecebido) status("")
                    Player.STATE_ENDED -> status("Transmissão encerrada")
                    else -> Unit
                }
            }
            override fun onPlayerError(error: PlaybackException) {
                status("Sem sinal: ${error.errorCodeName}")
            }
        }
        player.addListener(listener)
        try {
            val item = MediaItem.fromUri(url)
            if (url.startsWith("rtsp://", true)) {
                player.setMediaSource(RtspMediaSource.Factory()
                    .setForceUseRtpTcp(true)
                    .setTimeoutMs(12000)
                    .createMediaSource(item))
            } else {
                player.setMediaItem(item)
            }
            player.volume = 0f
            player.playWhenReady = true
            player.prepare()
        } catch (_: Exception) {
            status("Não foi possível iniciar a transmissão")
        }
        onDispose {
            player.removeListener(listener)
            player.release()
        }
    }
    AndroidView(
        factory = { ctx -> PlayerView(ctx).apply { useController = false; this.player = player } },
        update = { it.player = player },
        modifier = Modifier.fillMaxSize()
    )
}

/** Lê quadros JPEG diretamente do stream HTTP multipart, sem abrir páginas web. */
@Composable
private fun PlayerCameraMjpeg(url: String, onStatus: (String) -> Unit) {
    var quadro by remember(url) { mutableStateOf<Bitmap?>(null) }
    val status by rememberUpdatedState(onStatus)
    LaunchedEffect(url) {
        try {
            withContext(Dispatchers.IO) {
                val conexao = URL(url).openConnection() as HttpURLConnection
                try {
                    conexao.connectTimeout = 10000
                    conexao.readTimeout = 10000
                    conexao.instanceFollowRedirects = false
                    conexao.useCaches = false
                    conexao.setRequestProperty("Accept", "multipart/x-mixed-replace")
                    val codigo = conexao.responseCode
                    if (codigo !in 200..299) error("Servidor de vídeo retornou HTTP $codigo")
                    val tipo = conexao.contentType.orEmpty().lowercase()
                    if (!tipo.startsWith("multipart/x-mixed-replace")) {
                        error("O endereço HTTP não fornece MJPEG/JPEG")
                    }
                    conexao.inputStream.buffered(32768).use { input ->
                        val buffer = ByteArrayOutputStream()
                        var anterior = -1
                        var capturando = false
                        var ultimoQuadro = android.os.SystemClock.elapsedRealtime()
                        var bytesLidos = 0
                        while (true) {
                            currentCoroutineContext().ensureActive()
                            val atual = input.read()
                            if (atual < 0) throw EOFException()
                            bytesLidos++
                            if (bytesLidos % 4096 == 0 && android.os.SystemClock.elapsedRealtime() - ultimoQuadro > 15000) {
                                error("Nenhum quadro recebido")
                            }
                            if (!capturando && anterior == 0xff && atual == 0xd8) {
                                buffer.reset()
                                buffer.write(0xff)
                                buffer.write(0xd8)
                                capturando = true
                            } else if (capturando) {
                                buffer.write(atual)
                                if (buffer.size() > 5 * 1024 * 1024) error("Quadro de vídeo excede o limite")
                                if (anterior == 0xff && atual == 0xd9) {
                                    val dados = buffer.toByteArray()
                                    val opcoes = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                                    BitmapFactory.decodeByteArray(dados, 0, dados.size, opcoes)
                                    if (opcoes.outWidth <= 0 || opcoes.outHeight <= 0) error("Quadro JPEG inválido")
                                    var amostra = 1
                                    while (opcoes.outWidth / amostra > 1920 || opcoes.outHeight / amostra > 1080) amostra *= 2
                                    opcoes.inJustDecodeBounds = false
                                    opcoes.inSampleSize = amostra
                                    val bitmap = BitmapFactory.decodeByteArray(dados, 0, dados.size, opcoes)
                                    if (bitmap != null) {
                                        ultimoQuadro = android.os.SystemClock.elapsedRealtime()
                                        withContext(Dispatchers.Main) {
                                            quadro = bitmap
                                            status("")
                                        }
                                    }
                                    capturando = false
                                }
                            }
                            anterior = atual
                        }
                    }
                } finally { conexao.disconnect() }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (_: EOFException) {
            status("Transmissão encerrada")
        } catch (_: Exception) {
            status("Sem sinal HTTP/MJPEG. Verifique a rede e o endereço cadastrado.")
        }
    }
    quadro?.let {
        Image(bitmap = it.asImageBitmap(), contentDescription = "Imagem da câmera", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Fit)
    }
}

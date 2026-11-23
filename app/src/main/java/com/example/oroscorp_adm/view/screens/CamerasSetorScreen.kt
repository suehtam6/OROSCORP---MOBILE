package com.example.oroscorp_adm.view.screens

import androidx.compose.runtime.Composable
import com.example.oroscorp_adm.model.Camera
import com.example.oroscorp_adm.model.Setor
import com.example.oroscorp_adm.model.StatusCamera

/** Compatibilidade: reutiliza o painel existente e filtra a câmera pelo ID do setor. */
@Composable
fun CamerasSetorScreen(
    setor: Setor?,
    cameras: List<Camera> = emptyList(),
    statusCameras: List<StatusCamera> = emptyList(),
    onVoltar: () -> Unit
) {
    if (setor == null) {
        DetalhesSetorScreen(setorId = -1, onVoltar = onVoltar)
        return
    }
    DetalhesSetorScreen(
        setorId = setor.id,
        onVoltar = onVoltar,
        setores = listOf(SetorInfo(setor.id, setor.nome, setor.descricao, null,
            cameras.count { it.id_setor == setor.id }, null)),
        cameras = cameras.filter { it.id_setor == setor.id }.map {
            val st = statusCameras.firstOrNull { sc -> sc.id == it.id_status_camera }
            CameraInfo(it.id, it.nome, it.id_setor, st?.status.equals("online", true), st != null)
        }
    )
}


package com.example.oroscorp_adm.model

import com.example.oroscorp_adm.view.screens.OcorrenciasScreen
import com.example.oroscorp_adm.view.screens.RegistrarOcorrenciaScreen

data class Camera(
    val id: Int,
    val nome: String,
    val descricao: String,
    val endereco_stream: String,
    val id_setor: Int,
    val id_status_camera: Int
)
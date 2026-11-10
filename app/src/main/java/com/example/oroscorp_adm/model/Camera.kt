
package com.example.oroscorp_adm.model

import com.example.oroscorp_adm.view.screens.OcorrenciasScreen
import com.example.oroscorp_adm.view.screens.RegistrarOcorrenciaScreen

data class Camera(
    val id: Int,
    val nome: String,
    val setor: String,
    val ip: String,
    val online: Boolean,
    val pessoas: Int? = null,
    val alertas: Int? = null,
    val ocorrencias: Int? = null,
    val videoUrl: String? = null
)

package com.example.oroscorp_adm.model

import com.example.oroscorp_adm.view.screens.OcorrenciasScreen
import com.example.oroscorp_adm.view.screens.RegistrarOcorrenciaScreen

data class Camera(
    val idCamera: Int,
    val nome: String,
    val descricao: String,
    val enderecoStream: String,
    val idSetor: Int,
    val idStatusCamera: Int
)
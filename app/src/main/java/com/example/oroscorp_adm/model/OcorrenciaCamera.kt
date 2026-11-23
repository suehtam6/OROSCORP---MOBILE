package com.example.oroscorp_adm.model
import com.google.gson.annotations.SerializedName
data class OcorrenciaCamera(val id: Int, val id_ocorrencia: Int, val foto: String,
    @SerializedName("data_hora") val dataHora: String, val id_camera: Int? = null,
    val nome_camera: String? = null, val confianca_ia: Float? = null)

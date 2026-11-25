
package com.example.oroscorp_adm.model

import com.google.gson.annotations.SerializedName

data class RelatorioPdf(
    val id: Int,

    @SerializedName("id_ocorrencia")
    val idOcorrencia: Int,

    @SerializedName("public_id")
    val publicId: String,

    @SerializedName("secure_url")
    val secureUrl: String,

    @SerializedName("data_geracao")
    val dataGeracao: String? = null
)

data class RelatorioPdfResponse(
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int,

    val message: String? = null,

    val response: RelatorioPdf? = null
)

package com.example.oroscorp_adm.service

/** Resposta padronizada da API, conforme o POST de nivel-acesso fornecido. */
data class ApiEnvelope<T>(
    val response: T? = null,
    val status: Boolean? = null,
    val status_code: Int? = null,
    val message: String? = null
)

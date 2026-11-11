package com.example.oroscorp_adm.model

data class Usuario(
    val idUsuario: Int,
    val nome: String,
    val foto: String,
    val email: String,
    val senha: String,
    val dataNascimento: String,
    val idNivelAcesso: Int,
    val idStatusConta: Int
)

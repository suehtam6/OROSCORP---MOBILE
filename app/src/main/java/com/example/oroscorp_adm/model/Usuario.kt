package com.example.oroscorp_adm.model

data class Usuario(
    val id: Int,
    val nome: String,
    val foto: String,
    val email: String,
    val senha: String,
    val data_nascimento: String,
    val id_nivel_acesso: Int,
    val id_status_conta: Int
)

package com.example.oroscorp_adm.model


data class Funcionario(
    val id: Int,
    val nome: String,
    val data_nascimento: String,
    val cpf: String,
    val email: String,
    val foto: String,
    val id_cargo: Int,
    val id_setor: Int,
    val id_status_funcionario: Int,

)

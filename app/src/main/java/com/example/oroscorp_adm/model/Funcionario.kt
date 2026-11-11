package com.example.oroscorp_adm.model

import androidx.compose.ui.graphics.Color

data class Funcionario(
    val idFuncionario: Int,
    val nome: String,
    val dataNascimento: String,
    val cpf: String,
    val email: String,
    val foto: String,
    val idCargo: Int,
    val idSetor: Int,
    val idStatusFuncionario: Int,

    val cargo: String,
    val iniciais: String,
    val cor: Color
)

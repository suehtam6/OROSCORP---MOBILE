package com.example.oroscorp_adm.model

data class Ocorrencia(val id: Int, val id_setor: Int, val id_motivo_ocorrencia: Int, val id_status_ocorrencia: Int,
    val descricao: String? = null, val id_funcionario: Int? = null, val nome_funcionario: String? = null,
    val nome_origem: String? = null, val nome_prioridade: String? = null, val nome_setor: String? = null,
    val nome_motivo_ocorrencia: String? = null, val nome_status_ocorrencia: String? = null)

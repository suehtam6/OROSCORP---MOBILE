
package com.example.oroscorp_adm.view.screens

data class DashboardData(
    val ocorrenciasHoje: Int? = null,
    val aguardandoIdentificacao: Int? = null,
    val camerasOnline: Int? = null,
    val totalCameras: Int? = null,
    val ocorrenciasAbertas: Int? = null,
    val indiceSeguranca: Double? = null,
    val grafico: List<PontoDashboard> = emptyList(),
    val alertas: List<AlertaDashboard> = emptyList()
)

data class PontoDashboard(
    val rotulo: String,
    val quantidade: Int
)

data class AlertaDashboard(
    val id: Int,
    val descricao: String,
    val prioridade: String,
    val setor: String
)

package com.example.oroscorp_adm.view.screens

import com.example.oroscorp_adm.model.Ocorrencia
import com.example.oroscorp_adm.model.OcorrenciaCamera
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// Usa a data do cadastro confirmado neste aparelho; para os demais,
// usa a primeira evidência datada disponível na API.
data class ResumoDashboardOcorrencias(
    val hoje: Int?,
    val semData: Int?,
    val grafico: Map<String, List<PontoDashboard>>
)

object DashboardOcorrencias {
    private val fuso = ZoneId.of("America/Sao_Paulo")
    private val formatoDia = DateTimeFormatter.ofPattern("dd/MM")

    private fun interpretarData(valor: String?): LocalDateTime? {
        val texto = valor?.trim()?.replace(' ', 'T')
            ?.takeIf { it.isNotBlank() } ?: return null

        // Datas com offset representam um instante; datas SQL sem offset
        // já representam um horário local. Não altera o valor na API.
        return runCatching {
            OffsetDateTime.parse(texto)
                .atZoneSameInstant(fuso)
                .toLocalDateTime()
        }.getOrElse {
            runCatching { LocalDateTime.parse(texto) }.getOrNull()
        }
    }

    fun calcular(
        ocorrencias: List<Ocorrencia>,
        evidencias: List<OcorrenciaCamera>,
        dadosDisponiveis: Boolean,
        agora: LocalDateTime = LocalDateTime.now(fuso),
        datasCadastro: Map<Int, Instant> = emptyMap()
    ): ResumoDashboardOcorrencias {
        if (!dadosDisponiveis) {
            return ResumoDashboardOcorrencias(null, null, emptyMap())
        }

        val ids = ocorrencias.map { it.id }.toSet()
        val primeirasDatas = evidencias.asSequence()
            .filter { it.id_ocorrencia in ids }
            .mapNotNull { evidencia ->
                interpretarData(evidencia.dataHora)?.let {
                    evidencia.id_ocorrencia to it
                }
            }
            .groupBy({ it.first }, { it.second })
            .mapValues { (_, datas) -> datas.minOrNull()!! }
            .toMutableMap()

        datasCadastro.forEach { (id, instante) ->
            if (id in ids) {
                primeirasDatas[id] = LocalDateTime.ofInstant(instante, fuso)
            }
        }

        val semData = ids.size - primeirasDatas.size
        if (ids.isNotEmpty() && primeirasDatas.isEmpty()) {
            return ResumoDashboardOcorrencias(null, semData, emptyMap())
        }

        val hoje = agora.toLocalDate()
        val datas = primeirasDatas.values
        val datasHoje = datas.filter { it.toLocalDate() == hoje && !it.isAfter(agora) }
        val porDia = datas.filter { !it.isAfter(agora) }
            .groupingBy { it.toLocalDate() }.eachCount()
        val porHora = datasHoje.groupingBy { it.hour }.eachCount()

        fun dias(quantidade: Int): List<PontoDashboard> =
            (quantidade - 1 downTo 0).map { diasAtras ->
                val dia = hoje.minusDays(diasAtras.toLong())
                PontoDashboard(dia.format(formatoDia), porDia[dia] ?: 0)
            }

        return ResumoDashboardOcorrencias(
            hoje = datasHoje.size,
            semData = semData,
            grafico = mapOf(
                "Hoje" to (0..agora.hour).map { hora ->
                    PontoDashboard("${hora.toString().padStart(2, '0')}h", porHora[hora] ?: 0)
                },
                "Semana" to dias(7),
                "Mês" to dias(30)
            )
        )
    }
}

package com.example.oroscorp_adm.service

/** Altere para o IP LAN do computador se usar celular físico.
 *  Android Emulator: 10.0.2.2; host local: localhost.
 *  HTTP em rede local é apenas para desenvolvimento. Em produção, use HTTPS.
 */
object ApiConfig {
    const val BASE_URL = "http://10.0.2.2:8080/v1/oroscorp/"
}

/**
 * CONFIRMADOS: SETOR e NIVEL_ACESSO (conforme documentação fornecida).
 * NÃO CONFIRMADOS: demais caminhos são hipóteses de configuração; confirme
 * com a coleção Postman antes de exigir que funcionem no backend.
 * Em caso de 404 ou 405, corrija aqui, sem mexer nas Screens.
 */
object ApiPaths {
    const val SETOR = "setor"                     // GET confirmado
    const val NIVEL_ACESSO = "nivel-acesso"         // POST/DELETE confirmados
    const val LOGIN = "login"                     // NÃO CONFIRMADO
    const val USUARIO = "usuario"                 // NÃO CONFIRMADO
    const val ADMINISTRADOR = "administrador"                // NÃO CONFIRMADO
    const val RESPONSAVEL = "responsavel"          // NÃO CONFIRMADO
    const val CARGO = "cargo"                     // NÃO CONFIRMADO
    const val STATUS_SETOR = "status-setor"        // NÃO CONFIRMADO
    const val CAMERA = "camera"                   // NÃO CONFIRMADO
    const val STATUS_CAMERA = "status-camera"      // NÃO CONFIRMADO
    const val EPI = "epi"                         // NÃO CONFIRMADO
    const val SETOR_RESPONSAVEL = "setor-responsavel"
    const val OCORRENCIA_EPI = "ocorrencia-epi"
    const val OCORRENCIA_CAMERA = "ocorrencia-camera"
    const val OCORRENCIA = "ocorrencia"           // NÃO CONFIRMADO
    const val ORIGEM = "origem"
    const val PRIORIDADE = "prioridade"
    const val SETOR_EPI = "setor-epi"
    const val CATEGORIA_EPI = "categoria-epi"
    const val FUNCIONARIO = "funcionario"         // NÃO CONFIRMADO
    const val MOTIVO_OCORRENCIA = "motivo_ocorrencia"
    const val SETOR_FUNCIONARIO = "setor-funcionario"
    const val STATUS_CONTA = "status-conta"
    const val STATUS_FUNCIONARIO = "statusfuncionario"
    const val STATUS_OCORRENCIA = "status-ocorrencia"
}

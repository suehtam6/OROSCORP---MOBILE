package com.example.oroscorp_adm.repository

import com.example.oroscorp_adm.model.RelatorioPdf
import com.example.oroscorp_adm.service.ApiConfig
import com.example.oroscorp_adm.service.CloudinaryService
import com.example.oroscorp_adm.service.SessionAuth

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.io.File
import java.util.concurrent.TimeUnit

class RelatorioPdfRepository {

    private val cliente = OkHttpClient.Builder()
        .connectTimeout(
            15,
            TimeUnit.SECONDS
        )
        .readTimeout(
            90,
            TimeUnit.SECONDS
        )
        .writeTimeout(
            90,
            TimeUnit.SECONDS
        )
        .callTimeout(
            120,
            TimeUnit.SECONDS
        )
        .build()


    private val api: CloudinaryService =
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(cliente)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(
                CloudinaryService::class.java
            )


    private fun obterToken(): String {

        val token = SessionAuth.token

        if (token.isNullOrBlank()) {

            error(
                "Sua sessão expirou. Realize o login novamente."
            )
        }

        return "Bearer $token"
    }


    suspend fun enviarPdf(
        idOcorrencia: Int,
        arquivo: File

    ): RelatorioPdf {

        if (
            !arquivo.exists() ||
            arquivo.length() == 0L
        ) {

            error(
                "O arquivo PDF não foi encontrado ou está vazio."
            )
        }

        val limite = 10L * 1024 * 1024

        if (arquivo.length() > limite) {
            error("O PDF excede o limite de 10 MB.")
        }

        val requestBody = arquivo.asRequestBody(
            "application/pdf".toMediaType()
        )

        val multipart =
            MultipartBody.Part.createFormData(
                "arquivo",
                arquivo.name,
                requestBody
            )

        val response = api.enviarPdf(
            idOcorrencia = idOcorrencia,
            authorization = obterToken(),
            arquivo = multipart
        )

        if (!response.isSuccessful) {

            val mensagem = response.errorBody()
                ?.string()

            error(
                "Falha ao armazenar PDF. HTTP ${response.code()}: " +
                        mensagem.orEmpty()
            )
        }

        val resultado = response.body()
            ?: error(
                "O servidor não retornou os dados do relatório."
            )

        if (!resultado.status) {

            error(
                resultado.message
                    ?: "Não foi possível armazenar o PDF."
            )
        }

        return resultado.response
            ?: error(
                "O servidor não retornou o relatório armazenado."
            )
    }


    suspend fun buscarRelatorio(
        idOcorrencia: Int

    ): RelatorioPdf {

        val response = api.buscarRelatorio(
            idOcorrencia = idOcorrencia,
            authorization = obterToken()
        )

        if (!response.isSuccessful) {

            error(
                "Não foi possível consultar o relatório. HTTP ${response.code()}"
            )
        }

        val resultado = response.body()
            ?: error(
                "Resposta vazia do servidor."
            )

        if (!resultado.status) {

            error(
                resultado.message
                    ?: "Relatório indisponível."
            )
        }

        return resultado.response
            ?: error(
                "Nenhum relatório encontrado."
            )
    }
}

package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.RelatorioPdfResponse

import okhttp3.MultipartBody

import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Header
import retrofit2.http.GET

interface CloudinaryService {

    @Multipart
    @POST("relatorio-ocorrencia/{id}")
    suspend fun enviarPdf(
        @Path("id")
        idOcorrencia: Int,

        @Header("Authorization")
        authorization: String,

        @Part
        arquivo: MultipartBody.Part

    ): Response<RelatorioPdfResponse>


    @GET("relatorio-ocorrencia/{id}")
    suspend fun buscarRelatorio(
        @Path("id")
        idOcorrencia: Int,

        @Header("Authorization")
        authorization: String

    ): Response<RelatorioPdfResponse>
}

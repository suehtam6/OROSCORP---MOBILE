package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.StatusOcorrencia
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StatusOcorrenciaService {

    @POST("status-ocorrencia")
    fun PostStatusOcorrencia(@Path("id")id: Int): Call<StatusOcorrencia>

    @GET("status-ocorrencia")
    fun GetStatusOcorrencias(): Call<List<StatusOcorrencia>>

    @GET("status-ocorrencia/{id}")
    fun GetStatusOcorrencia(@Path("id")id: Int): Call<StatusOcorrencia>

    @PUT("status-ocorrencia/{id}")
    fun PutStatusOcorrencia(@Path("id")id: Int): Call<StatusOcorrencia>

    @DELETE("status-ocorrencia/{id}")
    fun DeleteStatusOcorrencia(@Path("id")id: Int): Call<StatusOcorrencia>
}
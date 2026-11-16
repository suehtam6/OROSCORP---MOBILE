package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.OcorrenciaEpi
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OcorrenciaEpiService {

    @POST("ocorrencia-epi")
    fun PostOcorrenciaEpi(@Path("id")id: Int): Call<OcorrenciaEpi>

    @GET("ocorrencia-epi")
    fun GetOcorrenciaEpis(): Call<List<OcorrenciaEpi>>

    @GET("ocorrencia-epi/{id}")
    fun GetOcorrenciaEpi(@Path("id")id: Int): Call<OcorrenciaEpi>

    @PUT("ocorrencia-epi/{id}")
    fun PutOcorrenciaEpi(@Path("id")id: Int): Call<OcorrenciaEpi>

    @DELETE("ocorrencia-epi/{id}")
    fun DeleteOcorrenciaEpi(@Path("id")id: Int): Call<OcorrenciaEpi>
}
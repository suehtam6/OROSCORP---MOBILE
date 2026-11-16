package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.Ocorrencia
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OcorrenciaService {

    @POST("ocorrencia")
    fun PostOcorrencia(@Path("id")id: Int): Call<Ocorrencia>

    @GET("ocorrencia")
    fun GetOcorrencias(): Call<List<Ocorrencia>>

    @GET("ocorrencia/{id}")
    fun GetOcorrencia(@Path("id")id: Int): Call<Ocorrencia>

    @PUT("ocorrencia/{id}")
    fun PutOcorrencia(@Path("id")id: Int): Call<Ocorrencia>

    @DELETE("ocorrencia/{id}")
    fun DeleteOcorrencia(@Path("id")id: Int): Call<Ocorrencia>
}
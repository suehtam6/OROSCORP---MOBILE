package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.MotivoOcorrencia
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MotivoOcorrenciaService {

    @POST("motivo-ocorrencia")
    fun PostMotivoOcorrencia(@Path("id")id: Int): Call<MotivoOcorrencia>

    @GET("motivo-ocorrencia")
    fun GetMotivoOcorrencias(): Call<List<MotivoOcorrencia>>

    @GET("motivo-ocorrencia/{id}")
    fun GetMotivoOcorrencia(@Path("id")id: Int): Call<MotivoOcorrencia>

    @PUT("motivo-ocorrencia/{id}")
    fun PutMotivoOcorrencia(@Path("id")id: Int): Call<MotivoOcorrencia>

    @DELETE("motivo-ocorrencia/{id}")
    fun DeleteMotivoOcorrencia(@Path("id")id: Int): Call<MotivoOcorrencia>
}
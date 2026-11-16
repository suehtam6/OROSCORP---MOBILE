package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.OcorrenciaCamera
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OcorrenciaCameraService {

    @POST("ocorrencia-camera")
    fun PostOcorrenciaCamera(@Path("id")id: Int): Call<OcorrenciaCamera>

    @GET("ocorrencia-camera")
    fun GetOcorrenciaCameras(): Call<List<OcorrenciaCamera>>

    @GET("ocorrencia-camera/{id}")
    fun GetOcorrenciaCamera(@Path("id")id: Int): Call<OcorrenciaCamera>

    @PUT("ocorrencia-camera/{id}")
    fun PutOcorrenciaCamera(@Path("id")id: Int): Call<OcorrenciaCamera>

    @DELETE("ocorrencia-camera/{id}")
    fun DeleteOcorrenciaCamera(@Path("id")id: Int): Call<OcorrenciaCamera>
}
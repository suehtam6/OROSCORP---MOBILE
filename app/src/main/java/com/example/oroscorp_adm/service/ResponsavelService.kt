package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.Responsavel
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ResponsavelService {

    @POST("responsavel")
    fun PostResponsavel(@Path("id")id: Int): Call<Responsavel>

    @GET("responsavel")
    fun GetResponsavels(): Call<List<Responsavel>>

    @GET("responsavel/{id}")
    fun GetResponsavel(@Path("id")id: Int): Call<Responsavel>

    @PUT("responsavel/{id}")
    fun PutResponsavel(@Path("id")id: Int): Call<Responsavel>

    @DELETE("responsavel/{id}")
    fun DeleteResponsavel(@Path("id")id: Int): Call<Responsavel>
}
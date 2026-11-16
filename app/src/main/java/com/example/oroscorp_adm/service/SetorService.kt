package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.Setor
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SetorService {

    @POST("setor")
    fun PostSetor(@Path("id")id: Int): Call<Setor>

    @GET("setor")
    fun GetSetores(): Call<List<Setor>>

    @GET("setor/{id}")
    fun GetSetor(@Path("id")id: Int): Call<Setor>

    @PUT("setor/{id}")
    fun PutSetor(@Path("id")id: Int): Call<Setor>

    @DELETE("id")
    fun DeleteSetor(@Path("id")id: Int): Call<Setor>
}
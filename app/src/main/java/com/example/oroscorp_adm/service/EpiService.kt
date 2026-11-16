package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.Epi
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EpiService {

    @POST("epi")
    fun PostEpi(@Path("id")id: Int): Call<Epi>

    @GET("epi")
    fun GetEpis(): Call<List<Epi>>

    @GET("epi/{id}")
    fun GetEpi(@Path("id")id: Int): Call<Epi>

    @PUT("epi/{id}")
    fun PutEpi(@Path("id")id: Int): Call<Epi>

    @DELETE("epi/{id}")
    fun DeleteEpi(@Path("id")id: Int): Call<Epi>
}
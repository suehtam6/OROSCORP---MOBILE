package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.SetorEpi
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SetorEpiService {

    @POST("setor-epi")
    fun PostSetorEpi(@Path("id")id: Int): Call<SetorEpi>

    @GET("setor-epi")
    fun GetSetorEpis(): Call<List<SetorEpi>>

    @GET("setor-epi/{id}")
    fun GetSetorEpi(@Path("id")id: Int): Call<SetorEpi>

    @PUT("setor-epi/{id}")
    fun PutSetorEpi(@Path("id")id: Int): Call<SetorEpi>

    @DELETE("setor-epi/{id}")
    fun DeleteSetorEpi(@Path("id")id: Int): Call<SetorEpi>
}
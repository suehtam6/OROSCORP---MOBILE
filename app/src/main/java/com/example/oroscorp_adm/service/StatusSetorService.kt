package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.StatusSetor
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StatusSetorService {

    @POST("status-setor")
    fun PostStatusSetor(@Path("id")id: Int): Call<StatusSetor>

    @GET("status-setor")
    fun GetStatusSetors(): Call<List<StatusSetor>>

    @GET("status-setor/{id}")
    fun GetStatusSetor(@Path("id")id: Int): Call<StatusSetor>

    @PUT("status-setor/{id}")
    fun PutStatusSetor(@Path("id")id: Int): Call<StatusSetor>

    @DELETE("status-setor/{id}")
    fun DeleteStatusSetor(@Path("id")id: Int): Call<StatusSetor>
}
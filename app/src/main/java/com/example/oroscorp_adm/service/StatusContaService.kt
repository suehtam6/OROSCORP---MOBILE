package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.StatusConta
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StatusContaService {

    @POST("status-conta")
    fun PostStatusConta(@Path("id")id: Int): Call<StatusConta>

    @GET("status-conta")
    fun GetStatusContas(): Call<List<StatusConta>>

    @GET("status-conta/{id}")
    fun GetStatusConta(@Path("id")id: Int): Call<StatusConta>

    @PUT("status-conta/{id}")
    fun PutStatusConta(@Path("id")id: Int): Call<StatusConta>

    @DELETE("status-conta/{id}")
    fun DeleteStatusConta(@Path("id")id: Int): Call<StatusConta>
}
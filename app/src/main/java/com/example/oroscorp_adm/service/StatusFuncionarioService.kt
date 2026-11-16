package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.StatusFuncionario
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StatusFuncionarioService {

    @POST("status-funcionario")
    fun PostStatusFuncionario(@Path("id")id: Int): Call<StatusFuncionario>

    @GET("status-funcionario")
    fun GetStatusFuncionarios(): Call<List<StatusFuncionario>>

    @GET("status-funcionario/{id}")
    fun GetStatusFuncionario(@Path("id")id: Int): Call<StatusFuncionario>

    @PUT("status-funcionario/{id}")
    fun PutStatusFuncionario(@Path("id")id: Int): Call<StatusFuncionario>

    @DELETE("status-funcionario/{id}")
    fun DeleteStatusFuncionario(@Path("id")id: Int): Call<StatusFuncionario>
}
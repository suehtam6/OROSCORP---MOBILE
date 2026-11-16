package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.Funcionario
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FuncionarioService {

    @POST("funcionario")
    fun PostFuncionario(@Path("id")id: Int): Call<Funcionario>

    @GET("funcionario")
    fun GetFuncionarios(): Call<List<Funcionario>>

    @GET("funcionario/{id}")
    fun GetFuncionario(@Path("id")id: Int): Call<Funcionario>

    @PUT("funcionario/{id}")
    fun PutFuncionario(@Path("id")id: Int): Call<Funcionario>

    @DELETE("funcionario/{id}")
    fun DeleteFuncionario(@Path("id")id: Int): Call<Funcionario>
}
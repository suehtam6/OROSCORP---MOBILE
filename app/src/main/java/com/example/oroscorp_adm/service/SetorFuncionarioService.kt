package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.SetorFuncionario
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SetorFuncionarioService {

    @POST("setor-funcionario")
    fun PostSetorFuncionario(@Path("id")id: Int): Call<SetorFuncionario>

    @GET("setor-funcionario")
    fun GetSetorFuncionarios(): Call<List<SetorFuncionario>>

    @GET("setor-funcionario/{id}")
    fun GetSetorFuncionario(@Path("id")id: Int): Call<SetorFuncionario>

    @PUT("setor-funcionario/{id}")
    fun PutSetorFuncionario(@Path("id")id: Int): Call<SetorFuncionario>

    @DELETE("setor-funcionario/{id}")
    fun DeleteSetorFuncionario(@Path("id")id: Int): Call<SetorFuncionario>
}
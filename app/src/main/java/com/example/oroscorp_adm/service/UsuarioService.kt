package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.Usuario
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsuarioService {

    @POST("usuario")
    fun PostUsuario(@Path("id")id: Int): Call<Usuario>

    @GET("usuario")
    fun GetUsuarios(): Call<List<Usuario>>

    @GET("usuario/{id}")
    fun GetUsuario(@Path("id")id: Int): Call<Usuario>

    @PUT("usuario/{id}")
    fun PutUsuario(@Path("id")id: Int): Call<Usuario>

    @DELETE("usuario/{id}")
    fun DeleteUsuario(@Path("id")id: Int): Call<Usuario>
}
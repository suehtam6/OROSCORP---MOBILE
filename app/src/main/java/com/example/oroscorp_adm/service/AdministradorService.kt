package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.Administrador
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AdministradorService {

        @POST("administrador")
        fun PostAdministrador(@Path("id")id: Int): Call<Administrador>

        @GET("administrador")
        fun GetAdministradors(): Call<List<Administrador>>

        @GET("administrador/{id}")
        fun GetAdministrador(@Path("id")id: Int): Call<Administrador>

        @PUT("administrador/{id}")
        fun PutAdministrador(@Path("id")id: Int): Call<Administrador>

        @DELETE("administrador/{id}")
        fun DeleteAdministrador(@Path("id")id: Int): Call<Administrador>
    }

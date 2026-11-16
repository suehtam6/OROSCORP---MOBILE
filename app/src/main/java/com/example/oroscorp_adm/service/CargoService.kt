package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.Cargo
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CargoService {

    @POST("cargo")
    fun PostCargo(@Path("id")id: Int): Call<Cargo>

    @GET("cargo")
    fun GetCargos(): Call<List<Cargo>>

    @GET("cargo/{id}")
    fun GetCargo(@Path("id")id: Int): Call<Cargo>

    @PUT("cargo/{id}")
    fun PutCargo(@Path("id")id: Int): Call<Cargo>

    @DELETE("cargo/{id}")
    fun DeleteCargo(@Path("id")id: Int): Call<Cargo>
}

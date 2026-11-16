package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.NivelAcesso
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NivelAcessoService {

    @POST("nivel-acesso")
    fun PostNivelAcesso(@Path("id")id: Int): Call<NivelAcesso>

    @GET("nivel-acesso")
    fun GetNivelAcessos(): Call<List<NivelAcesso>>

    @GET("nivel-acesso/{id}")
    fun GetNivelAcesso(@Path("id")id: Int): Call<NivelAcesso>

    @PUT("nivel-acesso/{id}")
    fun PutNivelAcesso(@Path("id")id: Int): Call<NivelAcesso>

    @DELETE("nivel-acesso/{id}")
    fun DeleteNivelAcesso(@Path("id")id: Int): Call<NivelAcesso>
}
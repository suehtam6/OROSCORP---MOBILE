package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.SetorResponsavel
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SetorResponsavelService {

    @POST("setor-responsavel")
    fun PostSetorResponsavel(@Path("id")id: Int): Call<SetorResponsavel>

    @GET("setor-responsavel")
    fun GetSetorResponsavels(): Call<List<SetorResponsavel>>

    @GET("setor-responsavel/{id}")
    fun GetSetorResponsavel(@Path("id")id: Int): Call<SetorResponsavel>

    @PUT("setor-responsavel/{id}")
    fun PutSetorResponsavel(@Path("id")id: Int): Call<SetorResponsavel>

    @DELETE("setor-responsavel/{id}")
    fun DeleteSetorResponsavel(@Path("id")id: Int): Call<SetorResponsavel>
}
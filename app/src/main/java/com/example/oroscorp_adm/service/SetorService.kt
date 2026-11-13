package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.Setor
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SetorService {
    @GET("setor")
    fun getSetores(): Call<List<Setor>>

    @GET("setor/{id}")
    fun getSetor(@Path("id")id: Int): Call<Setor>
}
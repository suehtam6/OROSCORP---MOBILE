package com.example.oroscorp_adm.service

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SetorService {
    @GET("setor") fun getSetores(): Call<JsonElement>
    @GET("setor/{id}") fun getSetor(@Path("id") id: Int): Call<JsonElement>
}

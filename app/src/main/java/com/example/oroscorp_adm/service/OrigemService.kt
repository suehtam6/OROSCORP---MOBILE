package com.example.oroscorp_adm.service

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

/** Recurso citado no índice do Postman; URL exata ainda não confirmada. */
interface OrigemService {
    @GET fun listar(@Url url: String): Call<JsonElement>
}

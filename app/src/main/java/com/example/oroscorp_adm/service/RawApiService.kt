package com.example.oroscorp_adm.service

import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Url

/** Rotas relativas configuráveis até obter o export completo do Postman. */
interface RawApiService {
    @GET suspend fun get(@Url path: String): Response<JsonElement>
    @POST suspend fun post(@Url path: String, @Body body: JsonElement): Response<JsonElement>
    @PUT suspend fun put(@Url path: String, @Body body: JsonElement): Response<JsonElement>
    @DELETE suspend fun delete(@Url path: String): Response<JsonElement>
}

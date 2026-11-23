package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.SetorResponsavel
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Url

/** Paths are configured in ApiPaths; generic signatures avoid guessing routes. */
interface SetorResponsavelService {
    @GET fun listar(@Url url: String): Call<JsonElement>
    @GET fun buscar(@Url url: String): Call<JsonElement>
    @POST fun cadastrar(@Url url: String, @Body value: SetorResponsavel): Call<JsonElement>
    @PUT fun atualizar(@Url url: String, @Body value: SetorResponsavel): Call<JsonElement>
    @DELETE fun excluir(@Url url: String): Call<JsonElement>
}

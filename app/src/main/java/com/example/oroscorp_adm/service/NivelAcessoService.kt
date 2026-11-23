package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.NivelAcesso
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.HTTP

data class NivelAcessoCadastro(val nome: String)

interface NivelAcessoService {
    @POST("nivel-acesso")
    fun cadastrar(@Body body: NivelAcessoCadastro): Call<ApiEnvelope<NivelAcesso>>

    // O exemplo da documentação inclui um corpo na requisição DELETE.
    @HTTP(method = "DELETE", path = "nivel-acesso/{id}", hasBody = true)
    fun excluirComCorpo(
        @Path("id") id: Int,
        @Body body: NivelAcessoCadastro
    ): Call<ApiEnvelope<Map<String, Any>>>

    @DELETE("nivel-acesso/{id}")
    fun excluir(@Path("id") id: Int): Call<ApiEnvelope<Map<String, Any>>>
}

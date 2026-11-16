package com.example.oroscorp_adm.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/** Single shared client. No credential logging. */
class RetrofitFactory {
    private val retrofit: Retrofit by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val t = SessionAuth.token
                val req = if (t == null) original else original.newBuilder()
                    .header("Authorization", "Bearer $t").build()
                chain.proceed(req)
            }
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun getRawService(): RawApiService = retrofit.create(RawApiService::class.java)
    fun getSetorService(): SetorService = retrofit.create(SetorService::class.java)
    fun getNivelAcessoService(): NivelAcessoService = retrofit.create(NivelAcessoService::class.java)

    fun getCameraService(): CameraService = retrofit.create(CameraService::class.java)
    fun getFuncionarioService(): FuncionarioService = retrofit.create(FuncionarioService::class.java)
    fun getOcorrenciaService(): OcorrenciaService = retrofit.create(OcorrenciaService::class.java)
    fun getUsuarioService(): UsuarioService = retrofit.create(UsuarioService::class.java)
}

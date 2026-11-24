
package com.example.oroscorp_adm.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitFactory {

    private val retrofit: Retrofit by lazy {

        val client = OkHttpClient.Builder()

            .addInterceptor { chain ->

                val original = chain.request()

                val token = SessionAuth.token

                val req = if (token == null) {
                    original
                } else {
                    original.newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                }

                val request = if (req.body != null) {
                    req.newBuilder()
                        .header("Content-Type", "application/json")
                        .build()
                } else {
                    req
                }

                val recuperacaoSenha =
                    request.url.encodedPath
                        .endsWith("/trocar-senha/solicitar")

                if (recuperacaoSenha) {

                    chain
                        .withConnectTimeout(
                            15,
                            TimeUnit.SECONDS
                        )
                        .withReadTimeout(
                            90,
                            TimeUnit.SECONDS
                        )
                        .withWriteTimeout(
                            30,
                            TimeUnit.SECONDS
                        )
                        .proceed(request)

                } else {

                    chain.proceed(request)

                }
            }

            .addNetworkInterceptor { chain ->

                val request = chain.request()

                chain.proceed(
                    if (request.body != null) {
                        request.newBuilder()
                            .header(
                                "Content-Type",
                                "application/json"
                            )
                            .build()
                    } else {
                        request
                    }
                )
            }

            .connectTimeout(10, TimeUnit.SECONDS)

            .readTimeout(20, TimeUnit.SECONDS)

            .build()

        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .client(client)
            .build()
    }

    fun getRawService(): RawApiService =
        retrofit.create(RawApiService::class.java)
}

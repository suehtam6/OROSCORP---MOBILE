package com.example.oroscorp_adm.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {

    private val BASE_URL = "http://localhost:8080/v1/oroscorp/"

    private val retrofitFactory = Retrofit
        .Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getSetorService(): SetorService{
        return retrofitFactory.create(SetorService::class.java)
    }
}
package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.StatusCamera
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StatusCameraService {

    @POST("status-camera")
    fun PostStatusCamera(@Path("id")id: Int): Call<StatusCamera>

    @GET("status-camera")
    fun GetStatusCameras(): Call<List<StatusCamera>>

    @GET("status-camera/{id}")
    fun GetStatusCamera(@Path("id")id: Int): Call<StatusCamera>

    @PUT("status-camera/{id}")
    fun PutStatusCamera(@Path("id")id: Int): Call<StatusCamera>

    @DELETE("status-camera/{id}")
    fun DeleteStatusCamera(@Path("id")id: Int): Call<StatusCamera>
}
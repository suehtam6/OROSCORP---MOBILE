package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.Camera
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CameraService {

    @POST("camera")
    fun PostCamera(@Path("id")id: Int): Call<Camera>

    @GET("camera")
    fun GetCameras(): Call<List<Camera>>

    @GET("camera/{id}")
    fun GetCamera(@Path("id")id: Int): Call<Camera>

    @PUT("camera/{id}")
    fun PutCamera(@Path("id")id: Int): Call<Camera>

    @DELETE("camera/{id}")
    fun DeleteCamera(@Path("id")id: Int): Call<Camera>
}

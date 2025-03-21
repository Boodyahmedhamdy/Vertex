package com.iti.vertex.data.sources.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    private val retrofit: Retrofit by lazy {

        Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}
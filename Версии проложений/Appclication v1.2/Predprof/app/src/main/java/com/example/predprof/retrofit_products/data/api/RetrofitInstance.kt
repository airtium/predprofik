package com.example.predprof.retrofit_products.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
class RetrofitInstance {
    companion object RetrofitInstance {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("http://ec2-3-253-100-1.eu-west-1.compute.amazonaws.com:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }

        val api: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }
    }
}
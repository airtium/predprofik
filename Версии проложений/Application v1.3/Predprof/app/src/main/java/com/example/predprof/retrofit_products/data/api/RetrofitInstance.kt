package com.example.predprof.retrofit_products.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
class RetrofitInstance {
    companion object RetrofitInstance { // инициализация доменного имени
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("http://ec2-63-33-192-125.eu-west-1.compute.amazonaws.com:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }

        val api: ApiService by lazy { // инициализация api
            retrofit.create(ApiService::class.java)
        }
    }
}
package com.example.predprof.retrofit_products.data.api

import com.example.predprof.retrofit_products.models.got_producs_model.ProductList
import com.example.predprof.retrofit_products.models.post_products_model.gotProductListItem
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("user/list?id_user=1")
    suspend fun getProductList(): Response<ProductList>

    @FormUrlEncoded
    @POST("product/add")
    suspend fun pushProduct(
        @Field("id_list") id_list: Int,
        @Field("id_productname") id_productname: Int,
        @Field("quantity") quantity: Int
    ): Response<gotProductListItem>

}
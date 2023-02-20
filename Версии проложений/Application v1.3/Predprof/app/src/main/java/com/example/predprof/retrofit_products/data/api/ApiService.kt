package com.example.predprof.retrofit_products.data.api

import com.example.predprof.retrofit_products.models.got_producs_model.ProductList
import com.example.predprof.retrofit_products.models.post_products_model.deletedProduct
import com.example.predprof.retrofit_products.models.post_products_model.editProductModel
import com.example.predprof.retrofit_products.models.post_products_model.gotProductListItem
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @GET("user/list?id_user=1") // структура запроса на получение списка имеющихся продуктов
    suspend fun getProductList(): Response<ProductList>

    @FormUrlEncoded
    @POST("product/add") // структура запроса на добавление продукта
    suspend fun pushProduct(
        @Field("id_list") id_list: Int,
        @Field("id_productname") id_productname: Int,
        @Field("quantity") quantity: Int
    ): Response<gotProductListItem>

    @POST("product/scan") // структура запроса на сканирование фотографии
    @Multipart
    suspend fun pushFile(
        @Part image: MultipartBody.Part
    )

    @FormUrlEncoded
    @POST("product/deletename") // структура запроса на удаление продукта
    suspend fun pushDeleteResponse(
        @Field("id_productname") id_productname: Int
    ): Response<deletedProduct>

    @FormUrlEncoded
    @POST("product/update") // структура запроса на изменения продукта
    suspend fun pushEditResponse(
        @Field("id_product") id_product: Int,
        @Field("id_productname") id_productname: Int,
        @Field("quantity") quantity: Int,
    ): Response<editProductModel>
}
package com.example.predprof.retrofit_products.data.repository

import com.example.predprof.retrofit_products.data.api.RetrofitInstance
import com.example.predprof.retrofit_products.models.got_producs_model.ProductList
import com.example.predprof.retrofit_products.models.post_products_model.gotProductListItem
import retrofit2.Response

class Repository {
    suspend fun getPrs(): Response<ProductList> {
        return RetrofitInstance.api.getProductList()
    }

    suspend fun pushPrs(id_list: Int, id_productname: Int, quantity: Int): Response<gotProductListItem> {
        //println("$id_list, $id_productname, $quantity")
        return RetrofitInstance.api.pushProduct(id_list, id_productname, quantity)
    }
}
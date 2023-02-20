package com.example.predprof.retrofit_products.data.repository

import com.example.predprof.retrofit_products.data.api.RetrofitInstance
import com.example.predprof.retrofit_products.models.got_producs_model.ProductList
import com.example.predprof.retrofit_products.models.post_products_model.deletedProduct
import com.example.predprof.retrofit_products.models.post_products_model.editProductModel
import com.example.predprof.retrofit_products.models.post_products_model.gotProductListItem
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.io.IOException

class Repository {
    suspend fun getPrs(): Response<ProductList> { // отправка в api запроса на получение списка продуктов
        return RetrofitInstance.api.getProductList()
    }

    suspend fun pushPrs(id_list: Int, id_productname: Int, quantity: Int): Response<gotProductListItem> { //отправка в api запроса на добавление продукта
        return RetrofitInstance.api.pushProduct(id_list, id_productname, quantity)
    }

    suspend fun uploadImage(img: File): Boolean { //отправка в api запроса на отправку фотографии
        return try {
            RetrofitInstance.api.pushFile(image = MultipartBody.Part.createFormData("file", img.name, img.asRequestBody()))
            true

        } catch (e: IOException) {
            e.printStackTrace()
            false
        } catch (e: HttpException) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteProduct(id_productname: Int): Response<deletedProduct> { //отправка в api запроса на удаление продукта
        return RetrofitInstance.api.pushDeleteResponse(id_productname)
    }

    suspend fun editProduct(id_product: Int, id_productname: Int, quantity: Int): Response<editProductModel>{ // отправка в api запроса на изменение продукта
        return RetrofitInstance.api.pushEditResponse(id_productname = id_productname, id_product = id_product, quantity = quantity)
    }
}
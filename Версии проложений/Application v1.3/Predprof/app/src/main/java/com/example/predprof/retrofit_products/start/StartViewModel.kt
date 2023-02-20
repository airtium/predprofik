package com.example.predprof.retrofit_products.start

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.predprof.retrofit_products.data.repository.Repository
import com.example.predprof.retrofit_products.models.got_producs_model.ProductList
import com.example.predprof.retrofit_products.models.post_products_model.deletedProduct
import com.example.predprof.retrofit_products.models.post_products_model.editProductModel
import com.example.predprof.retrofit_products.models.post_products_model.gotProductListItem
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File

class StartViewModel: ViewModel() {
    private var repo = Repository()
    val myProductsList: MutableLiveData<Response<ProductList>> = MutableLiveData()
    val myPostResponse: MutableLiveData<Response<gotProductListItem>> = MutableLiveData()
    val myPostDelete: MutableLiveData<Response<deletedProduct>> = MutableLiveData()
    val myPostEdit: MutableLiveData<Response<editProductModel>> = MutableLiveData()

    fun getMyList() {
        viewModelScope.launch { // наполнение запроса данными
            myProductsList.value = repo.getPrs()
        }
    }

    fun pushMyProduct(id_list: Int, id_productname: Int, quantity: Int) {
        viewModelScope.launch { // наполнение запроса данными
            myPostResponse.value = repo.pushPrs(id_list, id_productname, quantity)
        }
    }

    fun pushMyImage(img: File) {
        viewModelScope.launch { // наполнение запроса данными
            repo.uploadImage(img)
        }
    }

    fun deleteMyProduct(id_productname: Int) {
        viewModelScope.launch { // наполнение запроса данными
            myPostDelete.value = repo.deleteProduct(id_productname)
        }
    }

    fun editMyProduct(id_product: Int, id_productname: Int, quantity: Int) {
        viewModelScope.launch { // наполнение запроса данными
            myPostEdit.value = repo.editProduct(id_product = id_product, id_productname = id_productname, quantity = quantity)
        }
    }
}
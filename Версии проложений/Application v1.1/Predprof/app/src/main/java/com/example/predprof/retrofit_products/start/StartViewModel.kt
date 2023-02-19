package com.example.predprof.retrofit_products.start

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.predprof.retrofit_products.data.repository.Repository
import com.example.predprof.retrofit_products.models.got_producs_model.ProductList
import com.example.predprof.retrofit_products.models.post_products_model.gotProductListItem
import kotlinx.coroutines.launch
import retrofit2.Response

class StartViewModel: ViewModel() {
    var repo = Repository()
    val myProductsList: MutableLiveData<Response<ProductList>> = MutableLiveData()
    val myPostResponse: MutableLiveData<Response<gotProductListItem>> = MutableLiveData()

    fun getMyList() {
        viewModelScope.launch {
            myProductsList.value = repo.getPrs()
        }
    }

    fun pushMyProduct(id_list: Int, id_productname: Int, quantity: Int) {
        viewModelScope.launch {
            myPostResponse.value = repo.pushPrs(id_list, id_productname, quantity)
        }
    }
}
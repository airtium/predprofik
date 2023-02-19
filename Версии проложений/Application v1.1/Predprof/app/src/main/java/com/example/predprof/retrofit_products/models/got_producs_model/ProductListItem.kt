package com.example.predprof.retrofit_products.models.got_producs_model

data class ProductListItem(
    val id_list: Int,
    val id_product: Int,
    val id_user: Int,
    val name: String,
    val quantity: Int,
    val userName: String,
    val userNickname: String
)
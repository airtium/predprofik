package com.example.predprof.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.predprof.R
import com.example.predprof.databinding.ItemProductNameBinding
import com.example.predprof.retrofit_products.data.products_dict.ProductDict
import com.example.predprof.retrofit_products.models.got_producs_model.ProductListItem
import kotlinx.android.synthetic.main.item_product_name.view.*
import java.util.*

class ProductAdapter(val listener: Listener) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var productList = ArrayList<ProductListItem>() // будующий список продуктов

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) { // клас для отслеживания событий с элменетами списка
        val binding = ItemProductNameBinding.bind(view)

        fun bind(item: ProductListItem, listener: Listener) = with(binding) {
            productName.text = item.name
            itemView.setOnClickListener {
                listener.onCLick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder { // стандартный имплементируемы метод
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_name, parent, false)

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) { // стандартный имплементируемы метод
        holder.bind(productList[position], listener)
    }

    override fun getItemCount(): Int { // стандартный имплементируемы метод
        return productList.size
    }

    fun getMaxId(): Int { // для будующего
        var maxid = 0

        for (el in productList)
            if (el.id_product > maxid)
                maxid = el.id_product

        return maxid
    }

    @SuppressLint("NotifyDataSetChanged")
    fun productAdd(p: ProductListItem) { // добавлние продукта
        productList.add(p)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: ArrayList<ProductListItem>) { // загрузка листа
        productList = list
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteProduct(product: ProductListItem) { // удаление продукта
        val indexToDelete = productList.indexOfFirst { it.name == product.name }
        productList.removeAt(indexToDelete)
        notifyDataSetChanged()
    }

    @SuppressLint("notifyDataSetChanged")
    fun editNameOfProduct(product: ProductListItem, new_name: String) {  // изменение продукта
        val indexToEdit = productList.indexOfFirst { it.name == product.name }
        productList[indexToEdit].name = new_name
        notifyDataSetChanged()
    }

    interface Listener { // интерфейс для слушателя нажатий на карточку продукта в списке
        fun onCLick(item: ProductListItem)
    }
}
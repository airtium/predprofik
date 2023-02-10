package com.example.predprof

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.predprof.adapter.ProductAdapter
import com.example.predprof.databinding.ActivityMainBinding
import com.example.predprof.model.ProductModel
import com.example.predprof.prouct_dict.ProductDict
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.addproduct_dialog.*
import kotlinx.android.synthetic.main.addproduct_dialog.view.*

class MainActivity : AppCompatActivity() {
    lateinit var bindingClass : ActivityMainBinding
    lateinit var adapter: ProductAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var addBtn: Button
    var product_to_add = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        init()

        addBtn = bindingClass.add
        addBtn.setOnClickListener {
            val dialog = layoutInflater.inflate(R.layout.addproduct_dialog, null)

            val myDialog = Dialog(this)
            myDialog.setContentView(dialog)

            myDialog.setCancelable(true)

            myDialog.show()

            myDialog.save.setOnClickListener {
                myDialog.dismiss()
                product_to_add = dialog.editProductName.text.toString()
                adapter.productAdd(ProductModel(product_to_add, ProductDict().getId(product_to_add)))
            }
        }
    }

    private fun init() {
        bindingClass.apply {
            recyclerView = bindingClass.recycler
            adapter = ProductAdapter()
            recyclerView.adapter = adapter
        }
    }
}
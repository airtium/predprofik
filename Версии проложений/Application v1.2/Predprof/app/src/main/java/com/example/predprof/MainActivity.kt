package com.example.predprof

import android.Manifest
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.predprof.adapter.ProductAdapter
import com.example.predprof.databinding.ActivityMainBinding
import com.example.predprof.retrofit_products.data.products_dict.ProductDict
import com.example.predprof.retrofit_products.models.got_producs_model.ProductListItem
import com.example.predprof.retrofit_products.models.post_products_model.gotProductListItem
import com.example.predprof.retrofit_products.start.StartViewModel
import kotlinx.android.synthetic.main.addproduct_dialog.*
import kotlinx.android.synthetic.main.addproduct_dialog.save
import kotlinx.android.synthetic.main.addproduct_dialog.view.*
import kotlinx.android.synthetic.main.addproduct_dialog.view.editProductName
import kotlinx.android.synthetic.main.editproduct_dialog.*
import kotlinx.android.synthetic.main.editproduct_dialog.view.*

class MainActivity : AppCompatActivity(), ProductAdapter.Listener {
    lateinit var bindingClass : ActivityMainBinding
    lateinit var adapter: ProductAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var addBtn: Button
    lateinit var scanBtn: Button
    var product_to_add = ""
    lateinit var viewModel: StartViewModel
    lateinit var myDialog: Dialog
    lateinit var dialog: View
    lateinit var myEditDialog: Dialog
    lateinit var edit_dialog: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StartViewModel::class.java)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        init()
        viewModel.getMyList()
        viewModel.myProductsList.observe(this, { list ->
            list.body()?.let { adapter.setList(it) }
        })

        addBtn = bindingClass.add
        addBtn.setOnClickListener {
            initDialog()

            myDialog.save.setOnClickListener {
                dialogaAddProuct(myDialog, dialog)
            }
        }

        scanBtn = bindingClass.scan
        scanBtn.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initDialog() {
        dialog = layoutInflater.inflate(R.layout.addproduct_dialog, null)
        myDialog = Dialog(this)
        myDialog.setContentView(dialog)
        myDialog.setCancelable(true)
        myDialog.show()
    }

    private fun dialogaAddProuct(myDialog: Dialog, dialog: View) {
        myDialog.dismiss()
        product_to_add = dialog.editProductName.text.toString()
        adapter.productAdd(ProductListItem(1, ProductDict().getId(product_to_add), 1, product_to_add, 1, "Michael", "Michel_MESSI"))
        viewModel.pushMyProduct(1, ProductDict().getId(product_to_add), 1)
        viewModel.myPostResponse.observe(this, Observer { response ->
            println(response.toString())
        })
    }

    private fun init() {
        bindingClass.apply {
            recyclerView = bindingClass.recycler
            adapter = ProductAdapter(this@MainActivity)
            recyclerView.adapter = adapter
        }
    }

    override fun onCLick(item: ProductListItem) {
        edit_dialog = layoutInflater.inflate(R.layout.editproduct_dialog, null)
        myEditDialog = Dialog(this)
        myEditDialog.setContentView(edit_dialog)
        myEditDialog.setCancelable(true)
        myEditDialog.show()
        var flag = 0

        myEditDialog.remove.setOnClickListener {
            flag = 1
        }

        myEditDialog.save_ed.setOnClickListener {
            myEditDialog.dismiss()
            if (flag == 0) {
                adapter.editNameOfProduct(item, edit_dialog.to_editProductName.text.toString())
                viewModel.editMyProduct(item.id_product, ProductDict().getId(edit_dialog.to_editProductName.text.toString()), 1)
                Log.d("Мое", "ID_NAME : ${ProductDict().getId(edit_dialog.to_editProductName.text.toString())}   ID : ${item.id_product}")
            }
            if (flag == 1) {
                adapter.deleteProduct(item)
                var id_productname = ProductDict().getId(item.name)
                viewModel.deleteMyProduct(id_productname)
            }
        }




    }
}
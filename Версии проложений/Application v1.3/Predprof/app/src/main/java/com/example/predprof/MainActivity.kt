package com.example.predprof

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.predprof.adapter.ProductAdapter
import com.example.predprof.databinding.ActivityMainBinding
import com.example.predprof.retrofit_products.data.products_dict.ProductDict
import com.example.predprof.retrofit_products.models.got_producs_model.ProductListItem
import com.example.predprof.retrofit_products.start.StartViewModel
import kotlinx.android.synthetic.main.addproduct_dialog.save
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
        viewModel = ViewModelProvider(this).get(StartViewModel::class.java) //модель через которую подаются запросы на сервер
        bindingClass = ActivityMainBinding.inflate(layoutInflater) // viewBinding
        setContentView(bindingClass.root)
        init() // ининциализация RecyclerView
        viewModel.getMyList() // запрос на получение листа с продуктами
        viewModel.myProductsList.observe(this, { list -> // получение ответа
            list.body()?.let { adapter.setList(it) }
        })

        addBtn = bindingClass.add // кнопка добавления продукта
        addBtn.setOnClickListener {
            initDialog() // иницализация диалогового окна добавления

            myDialog.save.setOnClickListener {
                dialogaAddProuct(myDialog, dialog) // метод добавления продукта
            }
        }

        scanBtn = bindingClass.scan // кнопка сканирования продукта
        scanBtn.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java) // переход к активити с камерой
            startActivity(intent)
        }
    }

    private fun initDialog() {
        dialog = layoutInflater.inflate(R.layout.addproduct_dialog, null) // конкретная реализация инициализации диалога
        myDialog = Dialog(this)
        myDialog.setContentView(dialog)
        myDialog.setCancelable(true)
        myDialog.show()
    }

    private fun dialogaAddProuct(myDialog: Dialog, dialog: View) {
        myDialog.dismiss() // закрытие диалогового окна
        product_to_add = dialog.editProductName.text.toString() // имя нового продукта
        adapter.productAdd(ProductListItem(1, ProductDict().getId(product_to_add), 1, product_to_add, 1, "Michael", "Michel_MESSI")) // добавление в список
        viewModel.pushMyProduct(1, ProductDict().getId(product_to_add), 1) // запрос на добавление на сервер
        viewModel.myPostResponse.observe(this, Observer { response ->
            println(response.toString()) // рудимент
        })
    }

    private fun init() {  // конкретная реализация RecyclerView
        bindingClass.apply {
            recyclerView = bindingClass.recycler
            adapter = ProductAdapter(this@MainActivity)
            recyclerView.adapter = adapter
        }
    }

    override fun onCLick(item: ProductListItem) { // функция нажатия на карточку продукта в списке
        edit_dialog = layoutInflater.inflate(R.layout.editproduct_dialog, null) // иницализация дилогового окна
        myEditDialog = Dialog(this)
        myEditDialog.setContentView(edit_dialog)
        myEditDialog.setCancelable(true)
        myEditDialog.show()
        var flag = 0

        myEditDialog.remove.setOnClickListener { // проверка нажатия на remove
            flag = 1
        }

        myEditDialog.save_ed.setOnClickListener { // проверка нажатия на сохранения с учитываением нажималась ли remove
            myEditDialog.dismiss()
            if (flag == 0) {
                adapter.editNameOfProduct(item, edit_dialog.to_editProductName.text.toString()) // изменение в списке названия
                viewModel.editMyProduct(id_product = item.id_product, id_productname = ProductDict().getId(edit_dialog.to_editProductName.text.toString()), 1) // отправка на сервер запроса редактирования
                Log.d("Мое", "ID_NAME : ${ProductDict().getId(edit_dialog.to_editProductName.text.toString())}   ID : ${item.id_product}") // для личной проверка
            }
            if (flag == 1) {
                adapter.deleteProduct(item) // удаление из списка продукта
                val id_productname = ProductDict().getId(item.name)
                viewModel.deleteMyProduct(id_productname) // отправка на сервер запроса удаления
            }
        }
    }
}
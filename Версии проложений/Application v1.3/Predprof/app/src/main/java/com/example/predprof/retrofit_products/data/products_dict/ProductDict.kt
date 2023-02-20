package com.example.predprof.retrofit_products.data.products_dict

class ProductDict {
    var arr = listOf( // полный список продуктов
        "Печенье сладкое с маком",
        "Капуста брокколи",
        "Сыр полутвердый",
        "Кофе растворимый с добавлением молотого",
        "Творог мягкий 2%",
        "Тесто замороженное дрожжевое",
        "Молоко 3,2% пастеризованное",
        "Блинчики с мясом",
        "Сметана из топленых сливок 15%",
        "Чай черный листовой"
    )

    fun getId(name: String): Int { // получене id продукта соответсвуещего его имени
        var id: Int = 0

        for (i in 0..10)
            if (arr[i] == name) {
                id = i
                break
            }

        return id + 1
    }
}
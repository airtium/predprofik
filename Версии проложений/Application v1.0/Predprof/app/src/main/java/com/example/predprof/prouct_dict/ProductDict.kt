package com.example.predprof.prouct_dict

class ProductDict {
    var dict = arrayListOf("Молоко", "Печенье", "Рюказак", "Велосипед")

    fun getId(name: String): Int {
        var i = 0

        while (true) {
            if (dict[i] == name)
                break
        }
        return i
    }

}

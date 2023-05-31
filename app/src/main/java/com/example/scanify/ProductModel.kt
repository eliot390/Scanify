package com.example.scanify

data class ProductModel (
    var id: Int = getAutoID(),
    var name: String = "",
    var title: String = "",
    var price: String = ""
){
    companion object{
        private var IDCounter = 0
        fun getAutoID():Int{
            return ++IDCounter
        }
    }
}
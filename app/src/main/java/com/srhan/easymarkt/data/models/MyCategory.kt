package com.srhan.easymarkt.data.models

sealed class MyCategory(val category: String) {

    object Chair : MyCategory("Chair")
    object Cupboard : MyCategory("Cupboard")
    object Table : MyCategory("Table")
    object Accessory : MyCategory("Accessory")
    object Furniture : MyCategory("Furniture")
}
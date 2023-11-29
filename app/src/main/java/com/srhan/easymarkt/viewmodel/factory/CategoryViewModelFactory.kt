package com.srhan.easymarkt.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.srhan.easymarkt.data.models.MyCategory
import com.srhan.easymarkt.viewmodel.CategoryViewModel

class CategoryViewModelFactory constructor(
    private val firestore: FirebaseFirestore,
    private val myCategory: MyCategory
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(firestore, myCategory) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}
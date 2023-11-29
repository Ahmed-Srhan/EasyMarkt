package com.srhan.easymarkt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.srhan.easymarkt.data.models.MyCategory
import com.srhan.easymarkt.data.models.Product
import com.srhan.easymarkt.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel constructor(
    private val firestore: FirebaseFirestore,
    private val myCategory: MyCategory
) : ViewModel() {

    private val _offerProducts: MutableStateFlow<Resource<List<Product>>?> = MutableStateFlow(null)
    val offerProducts = _offerProducts.asStateFlow()
    private val _bestProducts: MutableStateFlow<Resource<List<Product>>?> = MutableStateFlow(null)
    val bestProducts = _bestProducts.asStateFlow()

    init {
        fetchOfferProducts()
        fetchBestProducts()

    }

    fun fetchBestProducts() = viewModelScope.launch {
        _bestProducts.emit(Resource.Loading())
        firestore.collection("Products")
            .whereEqualTo("category", myCategory.category)
            .whereEqualTo("offerPercentage", null)
            .get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Success(products))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchOfferProducts() = viewModelScope.launch {
        _offerProducts.emit(Resource.Loading())
        firestore.collection("Products")
            .whereEqualTo("category", myCategory.category)
            .whereNotEqualTo("offerPercentage", null)
            .get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _offerProducts.emit(Resource.Success(products))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _offerProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }


}
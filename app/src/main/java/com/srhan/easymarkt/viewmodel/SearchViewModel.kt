package com.srhan.easymarkt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.srhan.easymarkt.data.models.Product
import com.srhan.easymarkt.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {
    private val _search = MutableStateFlow<Resource<List<Product>>?>(null)
    val search = _search.asStateFlow()

    fun searchProducts(searchQuery: String) {
        viewModelScope.launch {
            _search.emit(Resource.Loading())
        }
        searchProductsOnProduct(searchQuery).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val productsList = it.result!!.toObjects(Product::class.java)
                viewModelScope.launch {
                    _search.emit(Resource.Success(productsList))
                }


            } else
                viewModelScope.launch {
                    _search.emit(Resource.Error(it.exception.toString()))
                }

        }
    }

    private val productsCollection = firestore.collection("products")

    private fun searchProductsOnProduct(searchQuery: String) =
        productsCollection
            .orderBy("name") // Order the results by the "name" field
            .startAt(searchQuery) // Start the query at the provided search query
            .endAt(searchQuery + "\uf8ff")

}
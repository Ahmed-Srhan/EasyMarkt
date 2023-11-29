package com.srhan.easymarkt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.srhan.easymarkt.data.models.CartProduct
import com.srhan.easymarkt.firebase.FirebaseCommon
import com.srhan.easymarkt.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel() {

    private val _addToCart = MutableStateFlow<Resource<CartProduct>?>(null)
    val addToCart = _addToCart.asStateFlow()

    //    fun addUpdateProductInCart(cartProduct: CartProduct) {
//        viewModelScope.launch { _addToCart.emit(Resource.Loading()) }
//        firestore.collection("user").document(auth.uid!!).collection("cart")
//            .whereEqualTo("product.id", cartProduct.product.id).get()
//            .addOnSuccessListener {
//                it.documents.let {
//                    if (it.isEmpty()) { //Add new product
//                        addNewProduct(cartProduct)
//                    } else {
//                        val product = it.first().toObject(CartProduct::class.java)
//                        if(product?.product == cartProduct.product && product.selectedColor == cartProduct.selectedColor && product.selectedSize== cartProduct.selectedSize){
//                            val documentId = it.first().id
//                            increaseQuantity(documentId, cartProduct)
//                        } else { //Add new product
//                            addNewProduct(cartProduct)
//                        }
//                    }
//                }
//            }.addOnFailureListener {
//                viewModelScope.launch { _addToCart.emit(Resource.Error(it.message.toString())) }
//            }
//    }
    fun addUpdateProductInCart(cartProduct: CartProduct) {
        viewModelScope.launch {
            try {
                _addToCart.emit(Resource.Loading())

                val userId = auth.uid
                if (userId != null) {
                    val querySnapshot = firestore.collection("user").document(userId)
                        .collection("cart")
                        .whereEqualTo("product.id", cartProduct.product.id)
                        .get()
                        .await()

                    val documents = querySnapshot.documents
                    if (documents.isEmpty()) {
                        // Add new product
                        addNewProduct(cartProduct)
                    } else {
                        val product = documents.first().toObject(CartProduct::class.java)
                        if (product?.product == cartProduct.product &&
                            product.selectedColor == cartProduct.selectedColor &&
                            product.selectedSize == cartProduct.selectedSize
                        ) {
                            // Increase quantity for existing product
                            val documentId = documents.first().id
                            increaseQuantity(documentId, cartProduct)
                        } else {
                            // Add new product
                            addNewProduct(cartProduct)
                        }
                    }
                } else {
                    _addToCart.emit(Resource.Error("User is not authenticated"))
                }
            } catch (e: Exception) {
                _addToCart.emit(Resource.Error(e.message.toString()))
            }
        }
    }

    private fun addNewProduct(cartProduct: CartProduct) {
        firebaseCommon.addProductToCart(cartProduct) { addedProduct, e ->
            viewModelScope.launch {
                if (e == null)
                    _addToCart.emit(Resource.Success(addedProduct!!))
                else
                    _addToCart.emit(Resource.Error(e.message.toString()))
            }
        }
    }

    private fun increaseQuantity(documentId: String, cartProduct: CartProduct) {
        firebaseCommon.increaseQuantity(documentId) { _, e ->
            viewModelScope.launch {
                if (e == null)
                    _addToCart.emit(Resource.Success(cartProduct))
                else
                    _addToCart.emit(Resource.Error(e.message.toString()))
            }
        }
    }
}











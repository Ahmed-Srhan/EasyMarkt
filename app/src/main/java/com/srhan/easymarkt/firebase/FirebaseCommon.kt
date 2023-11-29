package com.srhan.easymarkt.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.srhan.easymarkt.data.models.CartProduct

class FirebaseCommon(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    private val cartCollection: CollectionReference?
        get() = auth.uid?.let {
            firestore.collection("user").document(it).collection("cart")
        }

    // firestore.collection("user").document(auth.uid!!).collection("cart")

    //    fun addProductToCart(cartProduct: CartProduct, onResult: (CartProduct?, Exception?) -> Unit) {
//        cartCollection?.document()?.set(cartProduct)
//            ?.addOnSuccessListener {
//                onResult(cartProduct, null)
//            }?.addOnFailureListener {
//                onResult(null, it)
//            }
//    }
    fun addProductToCart(cartProduct: CartProduct, onResult: (CartProduct?, Exception?) -> Unit) {
        val collection = cartCollection
        if (collection != null) {
            collection.document().set(cartProduct)
                .addOnSuccessListener {
                    onResult(cartProduct, null)
                }.addOnFailureListener {
                    onResult(null, it)
                }
        } else {
            onResult(null, NullPointerException("auth.uid is null"))
        }
    }

    fun increaseQuantity(documentId: String, onResult: (String?, Exception?) -> Unit) {
        firestore.runTransaction { transition ->
            val documentRef = cartCollection?.document(documentId)
            val document = transition.get(documentRef!!)
            val productObject = document.toObject(CartProduct::class.java)
            productObject?.let { cartProduct ->
                val newQuantity = cartProduct.quantity + 1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef, newProductObject)
            }
        }.addOnSuccessListener {
            onResult(documentId, null)
        }.addOnFailureListener {
            onResult(null, it)
        }
    }

    fun decreaseQuantity(documentId: String, onResult: (String?, Exception?) -> Unit) {
        firestore.runTransaction { transition ->
            val documentRef = cartCollection?.document(documentId)
            val document = transition.get(documentRef!!)
            val productObject = document.toObject(CartProduct::class.java)
            productObject?.let { cartProduct ->
                val newQuantity = cartProduct.quantity - 1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef, newProductObject)
            }
        }.addOnSuccessListener {
            onResult(documentId, null)
        }.addOnFailureListener {
            onResult(null, it)
        }
    }

    enum class QuantityChanging {
        INCREASE, DECREASE
    }


}
package com.harsh.shophere.domain.models


data class CartItemModel(
    val cartItemId: String = "",
    val productId : String = "",
    val variantId : String = "",
    val selectedOptions: Map<String, String> = emptyMap(),
    var quantity: Int = 0,
    val name: String = "",
    val image : String = "",
    val price : String = "",
    val addedAt: Long= System.currentTimeMillis(),
)

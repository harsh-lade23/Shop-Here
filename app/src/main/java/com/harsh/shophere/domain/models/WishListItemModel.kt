package com.harsh.shophere.domain.models


data class WishListItemModel(
    var wishListItemId: String ="",
    val productId : String = "",
    val name: String = "",
    val image : String = "",
    val price : String = "",
    val addedAt: Long= System.currentTimeMillis(),
)
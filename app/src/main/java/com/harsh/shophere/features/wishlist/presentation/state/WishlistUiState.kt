package com.harsh.shophere.features.wishlist.presentation.state

import com.harsh.shophere.domain.models.WishListItemModel

data class WishlistUiState(
    val isLoading: Boolean = false,
    val wishlistItems: List<WishListItemModel> = emptyList(),
    val isProductInWishlist: Boolean = false,
    val errorMessage: String? = null
)

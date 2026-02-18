package com.harsh.shophere.features.cart.presentation.state

import com.harsh.shophere.domain.models.CartItemModel

data class CartUiState(
    val isLoading: Boolean = false,
    val cartItems: List<CartItemModel> = emptyList(),
    val totalPrice: Double = 0.0,
    val errorMessage: String? = null
)

package com.harsh.shophere.domain.repository

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.CartItemModel
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCart(): Flow<ResultState<List<CartItemModel>>>
    fun addToCart(cartDataModel: CartItemModel): Flow<ResultState<String>>
    fun removeFromCart(cartItemId: String): Flow<ResultState<String>>
    fun updateQuantity(cartItemId: String, quantity: Int): Flow<ResultState<String>>
    fun clearCart(): Flow<ResultState<String>>
}

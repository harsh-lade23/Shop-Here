package com.harsh.shophere.features.cart.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.CartItemModel
import com.harsh.shophere.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke(cartItem: CartItemModel): Flow<ResultState<String>> {
        return cartRepository.addToCart(cartItem)
    }
}

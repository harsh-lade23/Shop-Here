package com.harsh.shophere.features.cart.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoveFromCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke(cartItemId: String): Flow<ResultState<String>> {
        return cartRepository.removeFromCart(cartItemId)
    }
}

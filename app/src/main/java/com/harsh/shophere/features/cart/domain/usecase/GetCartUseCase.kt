package com.harsh.shophere.features.cart.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.CartItemModel
import com.harsh.shophere.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke(): Flow<ResultState<List<CartItemModel>>> {
        return cartRepository.getCart()
    }
}

package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.CartItemModel
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class IncrementCartQuantityUseCase @Inject constructor(
    private val repository: Repository
) {
    fun incrementCartQuantity(cartItemModel: CartItemModel): Flow<ResultState<String>>{
        return repository.addOneQuantityIntoTheCartItem(cartItemModel)
    }
}
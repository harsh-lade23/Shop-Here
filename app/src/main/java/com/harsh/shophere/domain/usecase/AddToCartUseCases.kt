package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.CartItemModel
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class AddToCartUseCase @Inject constructor(private val repository:Repository) {

    fun addToCart(cartDataModel: CartItemModel): Flow<ResultState<String>>{
        return repository.addToCart(cartDataModel)
    }

}
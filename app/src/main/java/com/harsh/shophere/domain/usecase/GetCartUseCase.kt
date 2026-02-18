package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.CartItemModel
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCartUseCase @Inject constructor(private val repository: Repository) {

    fun getCart(): Flow<ResultState<List<CartItemModel>>> {
        return repository.getCart()
    }

}
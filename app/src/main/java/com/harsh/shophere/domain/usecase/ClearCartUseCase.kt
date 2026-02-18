package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ClearCartUseCase @Inject constructor(
    private val repository: Repository
) {
    fun clearCart(): Flow<ResultState<String>>{
        return repository.clearAllCartItems()
    }
}
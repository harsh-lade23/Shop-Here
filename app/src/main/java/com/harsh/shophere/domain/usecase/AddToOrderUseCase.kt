package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.OrdersData
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class AddToOrderUseCase @Inject constructor(
    private var repository: Repository
) {
    fun addToOrderUseCase(ordersData: OrdersData): Flow<ResultState<String>>{
        return repository.addToOrder(ordersData)
    }
}
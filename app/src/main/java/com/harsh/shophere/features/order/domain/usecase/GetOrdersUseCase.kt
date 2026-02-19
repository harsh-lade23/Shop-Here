package com.harsh.shophere.features.order.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.OrdersData
import com.harsh.shophere.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(): Flow<ResultState<List<OrdersData>>> {
        return orderRepository.getAllOrders()
    }
}

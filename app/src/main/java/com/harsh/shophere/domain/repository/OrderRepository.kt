package com.harsh.shophere.domain.repository

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.OrdersData
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun placeOrder(ordersData: OrdersData): Flow<ResultState<String>>
    fun getAllOrders(): Flow<ResultState<List<OrdersData>>>
}

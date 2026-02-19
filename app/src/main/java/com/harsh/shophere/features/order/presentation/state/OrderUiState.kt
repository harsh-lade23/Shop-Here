package com.harsh.shophere.features.order.presentation.state

import com.harsh.shophere.domain.models.OrdersData

data class OrderUiState(
    val isLoading: Boolean = false,
    val isOrderPlaced: Boolean = false,
    val orderList: List<OrdersData> = emptyList(),
    val errorMessage: String? = null
)

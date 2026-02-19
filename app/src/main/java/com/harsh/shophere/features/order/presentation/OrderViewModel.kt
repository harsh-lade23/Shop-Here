package com.harsh.shophere.features.order.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.OrdersData
import com.harsh.shophere.features.order.domain.usecase.GetOrdersUseCase
import com.harsh.shophere.features.order.domain.usecase.PlaceOrderUseCase
import com.harsh.shophere.features.order.presentation.state.OrderUiState
import com.harsh.shophere.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Route
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val placeOrderUseCase: PlaceOrderUseCase,
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {

    private val _orderState = MutableStateFlow(OrderUiState())
    val orderState: StateFlow<OrderUiState> = _orderState.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            getOrdersUseCase().collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _orderState.value = _orderState.value.copy(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _orderState.value = _orderState.value.copy(
                            isLoading = false,
                            orderList = result.result,
                            errorMessage = null
                        )
                    }
                    is ResultState.Error -> {
                        _orderState.value = _orderState.value.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }
            }
        }
    }

    fun placeOrder(ordersData: OrdersData, navController: NavController) {
        viewModelScope.launch {
            placeOrderUseCase(ordersData).collect { result ->
                Log.d("Tracking place order", "placeOrder: result -> $result")
                when (result) {
                    is ResultState.Loading -> {
                        _orderState.value = _orderState.value.copy(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _orderState.value = _orderState.value.copy(
                            isLoading = false,
                            isOrderPlaced = true,
                            errorMessage = null
                        )
                        // Refresh order list after placing order
                        navController.currentBackStackEntry?.savedStateHandle?.set("orderData", ordersData)
                        navController.navigate(Routes.OrderConfirmationScreen)
                    }
                    is ResultState.Error -> {
                        _orderState.value = _orderState.value.copy(
                            isLoading = false,
                            isOrderPlaced = false,
                            errorMessage = result.error
                        )
                    }
                }
            }
        }
    }

    fun resetOrderPlacedState() {
        _orderState.value = _orderState.value.copy(isOrderPlaced = false)
    }

    fun clearError() {
        _orderState.value = _orderState.value.copy(errorMessage = null)
    }
}

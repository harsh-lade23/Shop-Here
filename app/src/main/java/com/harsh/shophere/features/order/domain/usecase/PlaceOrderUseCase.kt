package com.harsh.shophere.features.order.domain.usecase

import android.util.Log
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.OrdersData
import com.harsh.shophere.domain.repository.CartRepository
import com.harsh.shophere.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaceOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository
) {
     operator fun invoke(ordersData: OrdersData): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)

        // Place the order and collect all states
        orderRepository.placeOrder(ordersData).collect { result ->
            when (result) {
                is ResultState.Loading -> {
                    // Already emitted loading above
                    Log.d("Tracking place order", "placeOrderUsecase: loading")

                }
                is ResultState.Success -> {
                    // Emit success immediately to allow UI navigation
                    Log.d("Tracking place order", "placeOrderUsecase: Placed order successfully")

                    emit(result)



                    // Clear cart in background without blocking
                    cartRepository.clearCart().collect { }

                }
                is ResultState.Error -> {
                    Log.d("Tracking place order", "placeOrderUsecase: failed to Place order ")
                    emit(result)
                }
            }
        }
    }
}

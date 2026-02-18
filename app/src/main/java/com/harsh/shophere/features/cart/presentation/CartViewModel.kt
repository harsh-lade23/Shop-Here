package com.harsh.shophere.features.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.CartItemModel
import com.harsh.shophere.features.cart.domain.usecase.AddToCartUseCase
import com.harsh.shophere.features.cart.domain.usecase.ClearCartUseCase
import com.harsh.shophere.features.cart.domain.usecase.GetCartUseCase
import com.harsh.shophere.features.cart.domain.usecase.RemoveFromCartUseCase
import com.harsh.shophere.features.cart.domain.usecase.UpdateCartQuantityUseCase
import com.harsh.shophere.features.cart.presentation.state.CartUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val clearCartUseCase: ClearCartUseCase
) : ViewModel() {

    private val _cartState = MutableStateFlow(CartUiState())
    val cartState: StateFlow<CartUiState> = _cartState.asStateFlow()

    init {
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch {
            getCartUseCase().collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _cartState.value = _cartState.value.copy(isLoading = true)
                    }
                    is ResultState.Success -> {
                        val cartItems = result.result
                        val total = calculateTotal(cartItems)
                        _cartState.value = _cartState.value.copy(
                            isLoading = false,
                            cartItems = cartItems,
                            totalPrice = total,
                            errorMessage = null
                        )
                    }
                    is ResultState.Error -> {
                        _cartState.value = _cartState.value.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }
            }
        }
    }

    fun addToCart(cartItem: CartItemModel) {
        viewModelScope.launch {
            addToCartUseCase(cartItem).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        loadCart()
                    }
                    is ResultState.Error -> {
                        _cartState.value = _cartState.value.copy(
                            errorMessage = result.error
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    fun updateQuantity(cartItemId: String, newQuantity: Int) {
        if (newQuantity < 1) return

        viewModelScope.launch {
            updateCartQuantityUseCase(cartItemId, newQuantity).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        loadCart()
                    }
                    is ResultState.Error -> {
                        _cartState.value = _cartState.value.copy(
                            errorMessage = result.error
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    fun incrementQuantity(cartItemId: String, currentQuantity: Int) {
        updateQuantity(cartItemId, currentQuantity + 1)
    }

    fun decrementQuantity(cartItemId: String, currentQuantity: Int) {
        if (currentQuantity > 1) {
            updateQuantity(cartItemId, currentQuantity - 1)
        }
    }

    fun removeFromCart(cartItemId: String) {
        viewModelScope.launch {
            removeFromCartUseCase(cartItemId).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        loadCart()
                    }
                    is ResultState.Error -> {
                        _cartState.value = _cartState.value.copy(
                            errorMessage = result.error
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            clearCartUseCase().collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        loadCart()
                    }
                    is ResultState.Error -> {
                        _cartState.value = _cartState.value.copy(
                            errorMessage = result.error
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    private fun calculateTotal(cartItems: List<CartItemModel>): Double {
        return cartItems.sumOf { item ->
            val price = item.price.toDoubleOrNull() ?: 0.0
            price * item.quantity
        }
    }
}

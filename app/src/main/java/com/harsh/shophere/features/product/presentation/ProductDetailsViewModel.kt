package com.harsh.shophere.features.product.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.features.product.domain.usecase.GetProductByIdUseCase
import com.harsh.shophere.features.product.domain.usecase.GetVariantListUseCase
import com.harsh.shophere.features.product.presentation.state.ProductDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getVariantListUseCase: GetVariantListUseCase
) : ViewModel() {

    private val _productState = MutableStateFlow(ProductDetailsUiState())
    val productState: StateFlow<ProductDetailsUiState> = _productState.asStateFlow()

    fun loadProductDetails(productId: String) {
        Log.d("Firebase", "ProductDetailsViewModel: loadProductDetails - $productId")
        viewModelScope.launch {
            combine(
                getProductByIdUseCase(productId),
                getVariantListUseCase(productId)
            ) { productResult, variantResult ->
                Log.d("Firebase", "ProductDetailsViewModel: inside combine - product: ${productResult}, variant: ${variantResult}")
                when {
                    productResult is ResultState.Error -> {
                        ProductDetailsUiState(
                            isLoading = false,
                            errorMessage = productResult.error
                        )
                    }

                    variantResult is ResultState.Error -> {
                        ProductDetailsUiState(
                            isLoading = false,
                            variantErrorMessage = variantResult.error
                        )
                    }

                    variantResult is ResultState.Success && productResult is ResultState.Success -> {
                        Log.d("Firebase", "ProductDetailsViewModel: product ${productResult.result}, variant: ${variantResult.result}")

                        ProductDetailsUiState(
                            isLoading = false,
                            product = productResult.result,
                            variants = variantResult.result
                        )
                    }

                    else -> {
                        ProductDetailsUiState(
                            isLoading = true
                        )
                    }
                }
            }.collect { state ->
                _productState.value = state
            }
        }
    }
}

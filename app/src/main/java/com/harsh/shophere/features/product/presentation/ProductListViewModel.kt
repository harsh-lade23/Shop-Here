package com.harsh.shophere.features.product.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.features.product.domain.usecase.GetAllProductsUseCase
import com.harsh.shophere.features.product.domain.usecase.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _productState = MutableStateFlow<ProductListUiState>(ProductListUiState())
    val productState: StateFlow<ProductListUiState> = _productState.asStateFlow()

    init {
        loadAllProducts()
    }

    fun loadAllProducts() {
        viewModelScope.launch {
            getAllProductsUseCase().collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _productState.value = _productState.value.copy(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _productState.value = _productState.value.copy(
                            isLoading = false,
                            products = result.result,
                            errorMessage = null
                        )
                    }
                    is ResultState.Error -> {
                        _productState.value = _productState.value.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }
            }
        }
    }

    fun searchProducts(query: String) {
        if (query.isBlank()) {
            loadAllProducts()
            return
        }

        viewModelScope.launch {
            searchProductsUseCase(query).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _productState.value = _productState.value.copy(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _productState.value = _productState.value.copy(
                            isLoading = false,
                            searchResults = result.result,
                            errorMessage = null
                        )
                    }
                    is ResultState.Error -> {
                        _productState.value = _productState.value.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }
            }
        }
    }

    fun clearError() {
        _productState.value = _productState.value.copy(errorMessage = null)
    }
}

data class ProductListUiState(
    val isLoading: Boolean = false,
    val products: List<ProductsDataModel> = emptyList(),
    val searchResults: List<ProductsDataModel> = emptyList(),
    val errorMessage: String? = null
)

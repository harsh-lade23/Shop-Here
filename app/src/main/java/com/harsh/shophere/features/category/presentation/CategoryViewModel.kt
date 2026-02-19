package com.harsh.shophere.features.category.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.features.category.domain.usecase.GetProductsByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase
) : ViewModel() {

    private val _categoryProductState = MutableStateFlow(CategoryProductUiState())
    val categoryProductState: StateFlow<CategoryProductUiState> = _categoryProductState.asStateFlow()

    fun loadProductsByCategory(categoryId: String) {
        viewModelScope.launch {
            getProductsByCategoryUseCase(categoryId).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _categoryProductState.value = _categoryProductState.value.copy(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _categoryProductState.value = _categoryProductState.value.copy(
                            isLoading = false,
                            products = result.result,
                            errorMessage = null
                        )
                    }
                    is ResultState.Error -> {
                        _categoryProductState.value = _categoryProductState.value.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }
            }
        }
    }

    fun clearError() {
        _categoryProductState.value = _categoryProductState.value.copy(errorMessage = null)
    }
}

data class CategoryProductUiState(
    val isLoading: Boolean = false,
    val products: List<ProductsDataModel> = emptyList(),
    val errorMessage: String? = null
)

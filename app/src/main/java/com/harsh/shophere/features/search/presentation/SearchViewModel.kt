package com.harsh.shophere.features.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.features.product.domain.usecase.SearchProductsUseCase
import com.harsh.shophere.features.search.presentation.state.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _searchState = MutableStateFlow(SearchUiState())
    val searchState: StateFlow<SearchUiState> = _searchState.asStateFlow()

    fun searchProducts(query: String) {
        if (query.isBlank()) {
            _searchState.value = SearchUiState(products = emptyList())
            return
        }

        viewModelScope.launch {
            searchProductsUseCase(query).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _searchState.value = SearchUiState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _searchState.value = SearchUiState(
                            isLoading = false,
                            products = result.result
                        )
                    }
                    is ResultState.Error -> {
                        _searchState.value = SearchUiState(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }
            }
        }
    }
}

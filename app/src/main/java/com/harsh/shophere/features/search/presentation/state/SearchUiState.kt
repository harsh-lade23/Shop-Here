package com.harsh.shophere.features.search.presentation.state

import com.harsh.shophere.domain.models.ProductsDataModel

data class SearchUiState(
    val isLoading: Boolean = false,
    val products: List<ProductsDataModel?> = emptyList(),
    val errorMessage: String? = null
)

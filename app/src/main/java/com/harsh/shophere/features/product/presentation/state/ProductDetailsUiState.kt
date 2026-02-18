package com.harsh.shophere.features.product.presentation.state

import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.domain.models.VariantsDataModel

data class ProductDetailsUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val variantErrorMessage: String? = null,
    val product: ProductsDataModel? = null,
    val variants: List<VariantsDataModel>? = null
)

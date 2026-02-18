package com.harsh.shophere.features.home.presentation.state

import com.harsh.shophere.domain.models.BannerDataModel
import com.harsh.shophere.domain.models.CategoryDataModel
import com.harsh.shophere.domain.models.ProductsDataModel

data class HomeUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val categories: List<CategoryDataModel>? = null,
    val products: List<ProductsDataModel>? = null,
    val banners: List<BannerDataModel>? = null,
    val suggestedProducts: List<ProductsDataModel>? = null,
    val suggestedProductsError: String? = null
)

package com.harsh.shophere.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.features.banner.domain.usecase.GetBannersUseCase
import com.harsh.shophere.features.category.domain.usecase.GetCategoriesInLimitUseCase
import com.harsh.shophere.features.home.presentation.state.HomeUiState
import com.harsh.shophere.features.product.domain.usecase.GetProductsInLimitUseCase
import com.harsh.shophere.features.product.domain.usecase.GetSuggestedProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsInLimitUseCase: GetProductsInLimitUseCase,
    private val getCategoriesInLimitUseCase: GetCategoriesInLimitUseCase,
    private val getBannersUseCase: GetBannersUseCase,
    private val getSuggestedProductsUseCase: GetSuggestedProductsUseCase
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeUiState())
    val homeState: StateFlow<HomeUiState> = _homeState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            // Load main home data (products, categories, banners)
            combine(
                getProductsInLimitUseCase(),
                getCategoriesInLimitUseCase(),
                getBannersUseCase()
            ) { productResult, categoryResult, bannersResult ->
                when {
                    categoryResult is ResultState.Error -> {
                        HomeUiState(
                            isLoading = false,
                            errorMessage = categoryResult.error
                        )
                    }
                    productResult is ResultState.Error -> {
                        HomeUiState(
                            isLoading = false,
                            errorMessage = productResult.error
                        )
                    }
                    bannersResult is ResultState.Error -> {
                        HomeUiState(
                            isLoading = false,
                            errorMessage = bannersResult.error
                        )
                    }
                    categoryResult is ResultState.Success &&
                            productResult is ResultState.Success &&
                            bannersResult is ResultState.Success -> {
                        HomeUiState(
                            categories = categoryResult.result,
                            products = productResult.result,
                            banners = bannersResult.result,
                            isLoading = false
                        )
                    }
                    else -> {
                        HomeUiState(isLoading = true)
                    }
                }
            }.collect { state ->
                _homeState.value = state
            }
        }
    }

    fun loadSuggestedProducts() {
        viewModelScope.launch {
            getSuggestedProductsUseCase().collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        // Keep current state, just loading suggested products
                    }
                    is ResultState.Success -> {
                        _homeState.value = _homeState.value.copy(
                            suggestedProducts = result.result
                        )
                    }
                    is ResultState.Error -> {
                        _homeState.value = _homeState.value.copy(
                            suggestedProductsError = result.error
                        )
                    }
                }
            }
        }
    }
}

package com.harsh.shophere.features.wishlist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.WishListItemModel
import com.harsh.shophere.domain.repository.WishlistRepository
import com.harsh.shophere.features.wishlist.domain.usecase.AddToWishlistUseCase
import com.harsh.shophere.features.wishlist.domain.usecase.GetWishlistUseCase
import com.harsh.shophere.features.wishlist.domain.usecase.RemoveFromWishlistUseCase
import com.harsh.shophere.features.wishlist.domain.usecase.ToggleWishlistUseCase
import com.harsh.shophere.features.wishlist.presentation.state.WishlistUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val getWishlistUseCase: GetWishlistUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    private val toggleWishlistUseCase: ToggleWishlistUseCase,
    private val wishlistRepository: WishlistRepository
) : ViewModel() {

    private val _wishlistState = MutableStateFlow(WishlistUiState())
    val wishlistState: StateFlow<WishlistUiState> = _wishlistState.asStateFlow()

    init {
        loadWishlist()
    }

    fun loadWishlist() {
        viewModelScope.launch {
            getWishlistUseCase().collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _wishlistState.value = _wishlistState.value.copy(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _wishlistState.value = _wishlistState.value.copy(
                            isLoading = false,
                            wishlistItems = result.result,
                            errorMessage = null
                        )
                    }
                    is ResultState.Error -> {
                        _wishlistState.value = _wishlistState.value.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }
            }
        }
    }

    fun addToWishlist(wishListItemModel: WishListItemModel) {
        viewModelScope.launch {
            addToWishlistUseCase(wishListItemModel).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        loadWishlist()
                        _wishlistState.value = _wishlistState.value.copy(
                            isProductInWishlist = true
                        )
                    }
                    is ResultState.Error -> {
                        _wishlistState.value = _wishlistState.value.copy(
                            errorMessage = result.error
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    fun removeFromWishlist(wishListItemId: String) {
        viewModelScope.launch {
            removeFromWishlistUseCase(wishListItemId).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        loadWishlist()
                        _wishlistState.value = _wishlistState.value.copy(
                            isProductInWishlist = false
                        )
                    }
                    is ResultState.Error -> {
                        _wishlistState.value = _wishlistState.value.copy(
                            errorMessage = result.error
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    fun toggleWishlist(wishListItemModel: WishListItemModel) {
        viewModelScope.launch {
            // Optimistically flip the isProductInWishlist state immediately
            val currentState = _wishlistState.value.isProductInWishlist
            _wishlistState.value = _wishlistState.value.copy(
                isProductInWishlist = !currentState
            )

            toggleWishlistUseCase(wishListItemModel).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        loadWishlist()
                    }
                    is ResultState.Error -> {
                        // Revert the optimistic update on error
                        _wishlistState.value = _wishlistState.value.copy(
                            isProductInWishlist = currentState,
                            errorMessage = result.error
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    fun checkIfProductInWishlist(productId: String) {
        viewModelScope.launch {
            wishlistRepository.isInWishlist(productId).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _wishlistState.value = _wishlistState.value.copy(
                            isProductInWishlist = result.result
                        )
                    }
                    is ResultState.Error -> {
                        _wishlistState.value = _wishlistState.value.copy(
                            isProductInWishlist = false
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    fun clearError() {
        _wishlistState.value = _wishlistState.value.copy(errorMessage = null)
    }
}

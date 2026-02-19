package com.harsh.shophere.features.wishlist.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.WishListItemModel
import com.harsh.shophere.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ToggleWishlistUseCase @Inject constructor(
    private val repository: WishlistRepository,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase
) {
    suspend operator fun invoke(wishListItemModel: WishListItemModel): Flow<ResultState<String>> {
        val isInWishlistResult = repository.isInWishlist(wishListItemModel.productId).first()
        
        return if (isInWishlistResult is ResultState.Success && isInWishlistResult.result) {
            removeFromWishlistUseCase(wishListItemModel.wishListItemId)
        } else {
            addToWishlistUseCase(wishListItemModel)
        }
    }
}

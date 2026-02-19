package com.harsh.shophere.features.wishlist.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.WishListItemModel
import com.harsh.shophere.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddToWishlistUseCase @Inject constructor(
    private val repository: WishlistRepository
) {
    operator fun invoke(wishListItemModel: WishListItemModel): Flow<ResultState<String>> {
        return repository.addToWishlist(wishListItemModel)
    }
}

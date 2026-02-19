package com.harsh.shophere.features.wishlist.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoveFromWishlistUseCase @Inject constructor(
    private val repository: WishlistRepository
) {
    operator fun invoke(wishListItemId: String): Flow<ResultState<String>> {
        return repository.removeFromWishlist(wishListItemId)
    }
}

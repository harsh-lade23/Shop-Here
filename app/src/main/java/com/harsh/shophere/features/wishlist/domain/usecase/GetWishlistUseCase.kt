package com.harsh.shophere.features.wishlist.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.WishListItemModel
import com.harsh.shophere.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWishlistUseCase @Inject constructor(
    private val repository: WishlistRepository
) {
    operator fun invoke(): Flow<ResultState<List<WishListItemModel>>> {
        return repository.getWishlist()
    }
}

package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.WishListItemModel
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class RemoveItemFromWishListUseCase @Inject constructor(
    private val repository: Repository
) {
    fun removeItemFromWishlist(wishListItemModel: WishListItemModel): Flow<ResultState<String>>{
        return repository.removeItemFromWishlist(wishListItemModel)
    }
}
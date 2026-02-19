package com.harsh.shophere.domain.repository

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.WishListItemModel
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    fun getWishlist(): Flow<ResultState<List<WishListItemModel>>>
    fun addToWishlist(wishListItemModel: WishListItemModel): Flow<ResultState<String>>
    fun removeFromWishlist(wishListItemId: String): Flow<ResultState<String>>
    fun isInWishlist(productId: String): Flow<ResultState<Boolean>>
}

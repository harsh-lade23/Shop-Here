package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.WishListItemModel
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



class AddToWishlistUseCase @Inject constructor(private val repo: Repository) {

    fun addToWishList(wishlistItem: WishListItemModel): Flow<ResultState<String>>{
        return repo.addToWishlist(wishlistItem)
    }

}
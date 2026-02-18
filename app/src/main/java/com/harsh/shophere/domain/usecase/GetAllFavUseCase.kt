package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.WishListItemModel
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



class GetWishlistUseCase @Inject constructor(private val repository: Repository) {

    fun getWishlist(): Flow<ResultState<List<WishListItemModel>>>{
        return repository.getWishlist()
    }

}
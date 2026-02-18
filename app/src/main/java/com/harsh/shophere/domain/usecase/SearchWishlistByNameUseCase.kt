package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.WishListItemModel
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



class SearchWishlistByNameUseCase @Inject constructor(
    private val repository: Repository
) {
    fun searchWishlistByName(name: String): Flow<ResultState<List<WishListItemModel>>>{
        return repository.searchWishListByName(name)
    }
}
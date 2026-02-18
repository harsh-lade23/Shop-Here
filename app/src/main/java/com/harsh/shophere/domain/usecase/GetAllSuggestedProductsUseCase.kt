package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



class GetAllSuggestedProductListUseCase @Inject constructor(private val repository: Repository) {

    fun getAllSuggestedProductList(): Flow<ResultState<List<ProductsDataModel>>>{
        return repository.getAllSuggestedProducts()
    }

}
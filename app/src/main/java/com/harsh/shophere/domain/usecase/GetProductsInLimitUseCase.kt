package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetProductsInLimitUseCase @Inject constructor(private val repository: Repository) {

    fun getProductsInLimit(): Flow<ResultState<List<ProductsDataModel>>>{
        return repository.getProductsInLimit()
    }

}
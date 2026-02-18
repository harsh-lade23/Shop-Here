package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetProductByIdUseCase @Inject constructor(private val repository: Repository) {

    fun getProductById(productId:String): Flow<ResultState<ProductsDataModel>>{
        return repository.getProductById(productId)
    }

}
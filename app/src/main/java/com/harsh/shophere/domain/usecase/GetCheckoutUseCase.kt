package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.ProductsDataModel

import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCheckoutUseCase @Inject constructor(private val repository: Repository) {

    fun getCheckout(productId:String): Flow<ResultState<ProductsDataModel>>{
        return repository.getCheckout(productId)
    }

}
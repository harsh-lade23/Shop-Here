package com.harsh.shophere.domain.usecase;


import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



class GetAllProductUseCase @Inject constructor(private val repository: Repository) {

    fun getAllProducts(): Flow<ResultState<List<ProductsDataModel>>>{
        return repository.getAllProducts()
    }

}
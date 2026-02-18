package com.harsh.shophere.domain.repository

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.domain.models.VariantsDataModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProductsInLimit(): Flow<ResultState<List<ProductsDataModel>>>
    fun getAllProducts(): Flow<ResultState<List<ProductsDataModel>>>
    fun getProductById(productId: String): Flow<ResultState<ProductsDataModel>>
    fun searchProducts(query: String): Flow<ResultState<List<ProductsDataModel>>>
    fun getVariantList(productId: String): Flow<ResultState<List<VariantsDataModel>>>
    fun getSuggestedProducts(): Flow<ResultState<List<ProductsDataModel>>>
}

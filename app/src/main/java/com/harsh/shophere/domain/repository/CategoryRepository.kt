package com.harsh.shophere.domain.repository

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.CategoryDataModel
import com.harsh.shophere.domain.models.ProductsDataModel
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategoriesInLimit(): Flow<ResultState<List<CategoryDataModel>>>
    fun getAllCategories(): Flow<ResultState<List<CategoryDataModel>>>
    fun getProductsByCategory(categoryId: String): Flow<ResultState<List<ProductsDataModel>>>
    fun searchCategories(query: String): Flow<ResultState<List<CategoryDataModel>>>
}

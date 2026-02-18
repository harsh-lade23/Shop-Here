package com.harsh.shophere.features.category.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsByCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(categoryId: String): Flow<ResultState<List<ProductsDataModel>>> {
        return categoryRepository.getProductsByCategory(categoryId)
    }
}

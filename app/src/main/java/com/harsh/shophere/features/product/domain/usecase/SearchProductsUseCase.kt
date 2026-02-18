package com.harsh.shophere.features.product.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(query: String): Flow<ResultState<List<ProductsDataModel>>> {
        return productRepository.searchProducts(query)
    }
}

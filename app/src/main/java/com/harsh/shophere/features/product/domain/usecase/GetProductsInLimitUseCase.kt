package com.harsh.shophere.features.product.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsInLimitUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(): Flow<ResultState<List<ProductsDataModel>>> {
        return productRepository.getProductsInLimit()
    }
}

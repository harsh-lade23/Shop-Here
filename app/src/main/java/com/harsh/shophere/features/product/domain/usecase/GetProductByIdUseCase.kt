package com.harsh.shophere.features.product.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(productId: String): Flow<ResultState<ProductsDataModel>> {
        return productRepository.getProductById(productId)
    }
}

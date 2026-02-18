package com.harsh.shophere.features.product.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.VariantsDataModel
import com.harsh.shophere.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVariantListUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(productId: String): Flow<ResultState<List<VariantsDataModel>>> {
        return productRepository.getVariantList(productId)
    }
}

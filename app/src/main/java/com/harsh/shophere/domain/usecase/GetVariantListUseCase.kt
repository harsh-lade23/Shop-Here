package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.VariantsDataModel
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetVariantListUseCase @Inject constructor(
    private val repository: Repository
){
    fun getVariantList(productId: String): Flow<ResultState<List<VariantsDataModel>>> {
        return repository.getVariantList(productId)
    }
}
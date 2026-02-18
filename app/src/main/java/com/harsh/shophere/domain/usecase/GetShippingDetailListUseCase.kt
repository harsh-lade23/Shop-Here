package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.ShippingDetails
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetShippingDetailListUseCase @Inject constructor(
    private val repository: Repository
){


    fun getShippingDetailList (): Flow<ResultState<List<ShippingDetails>>> {
        return repository.getShippingDetailList( )
    }
}
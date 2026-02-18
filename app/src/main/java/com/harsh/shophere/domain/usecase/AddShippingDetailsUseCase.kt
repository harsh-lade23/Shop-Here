package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.ShippingDetails
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class AddShippingDetailsUseCase @Inject constructor(
    private val repository: Repository
){
    fun addShippingDetails(shippingDetails: ShippingDetails): Flow<ResultState<String>> {
        return repository.addShippingDetails(shippingDetails = shippingDetails)
    }
}
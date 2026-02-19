package com.harsh.shophere.features.user.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.ShippingDetails
import com.harsh.shophere.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddShippingAddressUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(shippingDetails: ShippingDetails): Flow<ResultState<String>> {
        return userRepository.addShippingAddress(shippingDetails)
    }
}

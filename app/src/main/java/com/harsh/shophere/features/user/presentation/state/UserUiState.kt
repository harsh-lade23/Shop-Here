package com.harsh.shophere.features.user.presentation.state

import com.harsh.shophere.domain.models.ShippingDetails
import com.harsh.shophere.domain.models.UserData

data class UserUiState(
    val isLoading: Boolean = false,
    val user: UserData? = null,
    val shippingAddresses: List<ShippingDetails> = emptyList(),
    val errorMessage: String? = null
)

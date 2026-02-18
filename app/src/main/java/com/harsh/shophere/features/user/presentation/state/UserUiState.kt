package com.harsh.shophere.features.user.presentation.state

data class UserUiState(
    val isLoading: Boolean = false,
    val user: com.harsh.shophere.domain.models.UserData? = null,
    val errorMessage: String? = null
)

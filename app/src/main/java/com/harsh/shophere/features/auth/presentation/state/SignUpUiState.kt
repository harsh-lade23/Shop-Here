package com.harsh.shophere.features.auth.presentation.state

data class SignUpUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null
)

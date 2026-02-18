package com.harsh.shophere.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.UserData
import com.harsh.shophere.features.auth.domain.usecase.LoginUseCase
import com.harsh.shophere.features.auth.domain.usecase.RegisterUseCase
import com.harsh.shophere.features.auth.presentation.state.LoginUiState
import com.harsh.shophere.features.auth.presentation.state.SignUpUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _signUpState = MutableStateFlow(SignUpUiState())
    val signUpState: StateFlow<SignUpUiState> = _signUpState.asStateFlow()

    private val _loginState = MutableStateFlow(LoginUiState())
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    fun register(userData: UserData, password: String) {
        viewModelScope.launch {
            registerUseCase(userData, password).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _signUpState.value = SignUpUiState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _signUpState.value = SignUpUiState(
                            isLoading = false,
                            userData = result.result
                        )
                    }
                    is ResultState.Error -> {
                        _signUpState.value = SignUpUiState(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }
            }
        }
    }

    fun login(userData: UserData, password: String) {
        viewModelScope.launch {
            loginUseCase(userData, password).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _loginState.value = LoginUiState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _loginState.value = LoginUiState(
                            isLoading = false,
                            userData = result.result
                        )
                    }
                    is ResultState.Error -> {
                        _loginState.value = LoginUiState(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }
            }
        }
    }
}

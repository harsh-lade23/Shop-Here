package com.harsh.shophere.features.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.features.user.domain.usecase.GetUserUseCase
import com.harsh.shophere.features.user.presentation.state.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _userState = MutableStateFlow(UserUiState())
    val userState: StateFlow<UserUiState> = _userState.asStateFlow()

    fun loadUser(uid: String) {
        viewModelScope.launch {
            getUserUseCase(uid).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _userState.value = UserUiState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _userState.value = UserUiState(
                            isLoading = false,
                            user = result.result
                        )
                    }
                    is ResultState.Error -> {
                        _userState.value = UserUiState(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }
            }
        }
    }
}

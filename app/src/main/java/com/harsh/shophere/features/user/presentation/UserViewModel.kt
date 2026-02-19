package com.harsh.shophere.features.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.ShippingDetails
import com.harsh.shophere.domain.models.UserData
import com.harsh.shophere.features.user.domain.usecase.AddShippingAddressUseCase
import com.harsh.shophere.features.user.domain.usecase.GetShippingAddressesUseCase
import com.harsh.shophere.features.user.domain.usecase.GetUserUseCase
import com.harsh.shophere.features.user.domain.usecase.UpdateProfileUseCase
import com.harsh.shophere.features.user.presentation.state.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val addShippingAddressUseCase: AddShippingAddressUseCase,
    private val getShippingAddressesUseCase: GetShippingAddressesUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private val _userState = MutableStateFlow(UserUiState())
    val userState: StateFlow<UserUiState> = _userState.asStateFlow()

    fun loadUser(uid: String) {
        viewModelScope.launch {
            getUserUseCase(uid).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _userState.value = _userState.value.copy(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _userState.value = _userState.value.copy(
                            isLoading = false,
                            user = result.result,
                            errorMessage = null
                        )
                    }
                    is ResultState.Error -> {
                        _userState.value = _userState.value.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }
            }
        }
    }

    fun loadShippingAddresses() {
        viewModelScope.launch {
            getShippingAddressesUseCase().collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _userState.value = _userState.value.copy(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _userState.value = _userState.value.copy(
                            isLoading = false,
                            shippingAddresses = result.result,
                            errorMessage = null
                        )
                    }
                    is ResultState.Error -> {
                        _userState.value = _userState.value.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }
            }
        }
    }

    fun saveShippingAddress(shippingDetails: ShippingDetails) {
        viewModelScope.launch {
            addShippingAddressUseCase(shippingDetails).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _userState.value = _userState.value.copy(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _userState.value = _userState.value.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                        // Refresh addresses after adding
                        loadShippingAddresses()
                    }
                    is ResultState.Error -> {
                        _userState.value = _userState.value.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }
            }
        }
    }

    fun updateProfile(userData: UserData) {
        viewModelScope.launch {
            updateProfileUseCase(userData).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _userState.value = _userState.value.copy(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _userState.value = _userState.value.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    is ResultState.Error -> {
                        _userState.value = _userState.value.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }
            }
        }
    }

    fun clearError() {
        _userState.value = _userState.value.copy(errorMessage = null)
    }
}

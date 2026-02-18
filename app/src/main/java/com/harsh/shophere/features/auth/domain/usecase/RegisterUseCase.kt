package com.harsh.shophere.features.auth.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.UserData
import com.harsh.shophere.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(
        userData: UserData,
        password: String
    ): Flow<ResultState<String>> {
        return authRepository.registerUserWithEmailAndPassword(userData, password)
    }
}

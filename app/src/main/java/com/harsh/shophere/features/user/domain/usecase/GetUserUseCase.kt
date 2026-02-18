package com.harsh.shophere.features.user.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.UserData
import com.harsh.shophere.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(uid: String): Flow<ResultState<UserData>> {
        return userRepository.getUserById(uid)
    }
}

package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.UserData
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class UpdateUserDataUseCase @Inject constructor(private val repository: Repository) {

    fun updateUserData(userData: UserData): Flow<ResultState<String>>{
        return repository.updateUserData(userData)
    }

}
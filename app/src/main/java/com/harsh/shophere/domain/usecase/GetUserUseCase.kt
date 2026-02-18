package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.UserDataParent
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetUserUseCase @Inject constructor(private val repository: Repository) {

    fun getUserById(uid: String): Flow<ResultState<UserDataParent>>{
        return repository.getUserById(uid)
    }

}
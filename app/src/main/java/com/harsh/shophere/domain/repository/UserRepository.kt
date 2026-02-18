package com.harsh.shophere.domain.repository

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.UserData
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserById(uid: String): Flow<ResultState<UserData>>
}

package com.harsh.shophere.domain.repository

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.UserData
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun registerUserWithEmailAndPassword(
        userData: UserData,
        password: String
    ): Flow<ResultState<String>>

    fun loginUserWithEmailAndPassword(
        userData: UserData,
        password: String
    ): Flow<ResultState<String>>
}

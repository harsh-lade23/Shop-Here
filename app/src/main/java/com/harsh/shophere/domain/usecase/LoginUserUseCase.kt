package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.UserData
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class LoginUserUseCase @Inject constructor(private val repository: Repository) {

    fun loginUser(userData: UserData, password:String): Flow<ResultState<String>>{
        return repository.loginUserWithEmailAndPassWord(userData, password)
    }

}
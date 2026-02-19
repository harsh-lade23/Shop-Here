package com.harsh.shophere.domain.repository

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.UserData
import com.harsh.shophere.domain.models.ShippingDetails
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserById(uid: String): Flow<ResultState<UserData>>
    fun addShippingAddress(shippingDetails: ShippingDetails): Flow<ResultState<String>>
    fun getShippingAddresses(): Flow<ResultState<List<ShippingDetails>>>
    fun updateUserProfile(userData: UserData): Flow<ResultState<String>>
}

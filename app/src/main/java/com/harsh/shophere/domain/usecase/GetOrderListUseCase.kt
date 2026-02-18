package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.OrdersData
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetOrderListUseCase @Inject constructor(
    private val repository: Repository
){
    fun getOrderList(): Flow<ResultState<List<OrdersData>>>{
        return repository.getOrderList()
    }
}
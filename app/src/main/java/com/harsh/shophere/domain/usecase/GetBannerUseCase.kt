package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.BannerDataModel
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBannerListUseCase @Inject constructor(private val repository: Repository) {

    fun getBannerList(): Flow<ResultState<List<BannerDataModel>>>{
        return repository.getBanners()
    }

}
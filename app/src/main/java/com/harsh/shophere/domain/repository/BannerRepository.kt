package com.harsh.shophere.domain.repository

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.BannerDataModel
import kotlinx.coroutines.flow.Flow

interface BannerRepository {
    fun getBanners(): Flow<ResultState<List<BannerDataModel>>>
}

package com.harsh.shophere.features.banner.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.BannerDataModel
import com.harsh.shophere.domain.repository.BannerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBannersUseCase @Inject constructor(
    private val bannerRepository: BannerRepository
) {
    operator fun invoke(): Flow<ResultState<List<BannerDataModel>>> {
        return bannerRepository.getBanners()
    }
}

package com.harsh.shophere.data.di

import com.harsh.shophere.data.repository.BannerRepositoryImpl
import com.harsh.shophere.domain.repository.BannerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BannerRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindBannerRepository(
        bannerRepositoryImpl: BannerRepositoryImpl
    ): BannerRepository
}

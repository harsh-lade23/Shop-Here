package com.harsh.shophere.data.di

import com.harsh.shophere.data.repository.WishlistRepositoryImpl
import com.harsh.shophere.domain.repository.WishlistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WishlistRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWishlistRepository(
        wishlistRepositoryImpl: WishlistRepositoryImpl
    ): WishlistRepository
}

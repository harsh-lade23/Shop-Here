package com.harsh.shophere.data.di

import com.harsh.shophere.data.repository.CategoryRepositoryImpl
import com.harsh.shophere.domain.repository.CategoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CategoryRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository
}

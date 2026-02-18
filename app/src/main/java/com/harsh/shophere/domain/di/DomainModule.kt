package com.harsh.shophere.domain.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.harsh.shophere.data.repo.RepositoryImplementation
import com.harsh.shophere.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module

@InstallIn(SingletonComponent::class)
class MyDomainModule {



    @Provides
    fun provideRepository(firebaseAuth: FirebaseAuth,
                           firebaseFirestore: FirebaseFirestore,
                          firebaseStorage: FirebaseStorage
    ): Repository{
        return RepositoryImplementation(firebaseAuth, firebaseFirestore, firebaseStorage)
    }
}
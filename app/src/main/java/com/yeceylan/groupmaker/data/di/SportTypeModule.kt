package com.yeceylan.groupmaker.data.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import com.yeceylan.groupmaker.data.repository.SportTypeRepositoryImpl
import com.yeceylan.groupmaker.domain.repository.SportTypeRepository
import com.yeceylan.groupmaker.domain.use_cases.sport_type.GetSportTypeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSportTypeRef() = Firebase.firestore.collection("typeCollection")

    @Provides
    @Singleton
    fun provideBooksRepository(
        sportTypeRef: CollectionReference
    ): SportTypeRepository = SportTypeRepositoryImpl(sportTypeRef)

    @Provides
    @Singleton
    fun provideUseCases(repo: SportTypeRepository) = GetSportTypeUseCase(repo)
}
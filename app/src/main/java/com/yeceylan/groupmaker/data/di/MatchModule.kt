package com.yeceylan.groupmaker.data.di

import com.google.firebase.firestore.FirebaseFirestore
import com.yeceylan.groupmaker.data.repository.MatchRepositoryImpl
import com.yeceylan.groupmaker.domain.repository.MatchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MatchModule {

    @Provides
    @Singleton
    fun provideMatchRepository(
        firestore: FirebaseFirestore
    ): MatchRepository {
        return MatchRepositoryImpl(firestore)
    }
}

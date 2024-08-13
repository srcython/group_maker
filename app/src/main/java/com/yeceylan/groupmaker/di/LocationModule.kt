package com.yeceylan.groupmaker.di

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.yeceylan.groupmaker.data.repository.LocationRepositoryImpl
import com.yeceylan.groupmaker.domain.repository.LocationRepository
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository

    companion object {

        @Provides
        @Singleton
        fun providePlacesClient(@ApplicationContext context: Context): PlacesClient {
            Places.initialize(context, "AIzaSyDZcw1a01QHvTigUvaQ1H_z9WvtEiZV8ko")
            return Places.createClient(context)
        }
    }
}

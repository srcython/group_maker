package com.yeceylan.groupmaker.data.repository

import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.yeceylan.groupmaker.domain.repository.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val placesClient: PlacesClient
) : LocationRepository {

    override suspend fun searchLocations(query: String): List<AutocompletePrediction> {
        return withContext(Dispatchers.IO) {
            val token = AutocompleteSessionToken.newInstance()
            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setSessionToken(token)
                .setCountries("TR")
                .setTypesFilter(listOf("establishment"))
                .build()

            val response = placesClient.findAutocompletePredictions(request)
                .await()

            response.autocompletePredictions
        }
    }
}

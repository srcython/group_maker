package com.yeceylan.groupmaker.domain.repository

import com.google.android.libraries.places.api.model.AutocompletePrediction

interface LocationRepository {
    suspend fun searchLocations(query: String): List<AutocompletePrediction>
}

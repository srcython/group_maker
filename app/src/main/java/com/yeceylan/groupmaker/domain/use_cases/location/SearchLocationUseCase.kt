package com.yeceylan.groupmaker.domain.use_cases.location

import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.yeceylan.groupmaker.domain.repository.LocationRepository
import javax.inject.Inject

class SearchLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(query: String): List<AutocompletePrediction> {
        return locationRepository.searchLocations(query)
    }
}

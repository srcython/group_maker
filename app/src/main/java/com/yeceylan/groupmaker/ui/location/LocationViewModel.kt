package com.yeceylan.groupmaker.ui.location

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.yeceylan.groupmaker.domain.use_cases.location.SearchLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val searchLocationUseCase: SearchLocationUseCase,
    private val placesClient: PlacesClient
) : ViewModel() {

    private val _selectedLocation = MutableStateFlow<LatLng?>(null)
    val selectedLocation: StateFlow<LatLng?> = _selectedLocation

    private val _selectedAddress = MutableStateFlow<String?>(null)
    val selectedAddress: StateFlow<String?> = _selectedAddress

    private val _predictions = MutableStateFlow<List<AutocompletePrediction>>(emptyList())
    val predictions: StateFlow<List<AutocompletePrediction>> = _predictions

    fun searchLocations(query: String) {
        viewModelScope.launch {
            try {
                val results = searchLocationUseCase(query)
                _predictions.value = results
            } catch (e: Exception) {
                Log.e("LocationViewModel", "Error searching locations", e)
            }
        }
    }

    fun fetchLocationDetails(placeId: String) {
        viewModelScope.launch {
            val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
            val request = FetchPlaceRequest.newInstance(placeId, placeFields)

            try {
                val placeResponse = placesClient.fetchPlace(request).await()
                val place = placeResponse.place

//                // Lat ve long dört ondalık basamakta format
//                val formattedLatLng = place.latLng?.let { latLng ->
//                    LatLng(
//                        String.format("%.4f", latLng.latitude).toDouble(),
//                        String.format("%.4f", latLng.longitude).toDouble()
//                    )
//                }

                _selectedLocation.value = place.latLng
                _selectedAddress.value = place.address

            } catch (e: Exception) {
                Log.e("LocationViewModel", "Error fetching place details", e)
            }
        }
    }
}

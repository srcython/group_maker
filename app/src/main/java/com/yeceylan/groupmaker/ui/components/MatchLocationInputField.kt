package com.yeceylan.groupmaker.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.ui.location.LocationViewModel

@Composable
fun MatchLocationInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    viewModel: LocationViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf(value) }
    val predictions by viewModel.predictions.collectAsState()
    var isDropdownOpen by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    // Update search results based on the query
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            viewModel.searchLocations(searchQuery)
        }
    }

    // Observe selected location
    val selectedLocation by viewModel.selectedLocation.collectAsState()

    Column {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onValueChange(it)
                isDropdownOpen = it.isNotEmpty()
            },
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            singleLine = true,
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = {
                        searchQuery = ""
                        onValueChange("")
                        isDropdownOpen = false
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "Clear",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = "Search",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        )

        if (isDropdownOpen && predictions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            ) {
                items(predictions) { prediction ->
                    PredictionItem(prediction) { selectedLocationName ->
                        // Fetch details for the selected place
                        viewModel.fetchLocationDetails(prediction.placeId)
                        searchQuery = selectedLocationName
                        onValueChange(selectedLocationName)
                        isDropdownOpen = false
                        focusManager.clearFocus()
                        Log.d("MatchLocationInputField", "Selected location: $selectedLocationName")
                    }
                }
            }
        }

        selectedLocation?.let { location ->
            val formattedLatitude = String.format("%.4f", location.latitude)
            val formattedLongitude = String.format("%.4f", location.longitude)

            Text(
                text = "Latitude: $formattedLatitude, Longitude: $formattedLongitude",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun PredictionItem(prediction: AutocompletePrediction, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(
                    prediction
                        .getPrimaryText(null)
                        .toString()
                )
            }
            .padding(8.dp)
    ) {
        Text(
            text = prediction.getPrimaryText(null).toString(),
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            text = prediction.getSecondaryText(null).toString(),
            style = MaterialTheme.typography.body2,
            color = Color.Gray
        )
    }
}

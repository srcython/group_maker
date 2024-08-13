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
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.ui.location.LocationViewModel
import com.yeceylan.groupmaker.ui.theme.Dimen

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

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            viewModel.searchLocations(searchQuery)
        }
    }

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
                .padding(vertical = Dimen.spacing_xxs),
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
                            modifier = Modifier.size(Dimen.spacing_m2)
                        )
                    }
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = "Search",
                        modifier = Modifier.size(Dimen.spacing_m2)
                    )
                }
            }
        )

        if (isDropdownOpen && predictions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(Dimen.spacing_xxxs, Color.Gray, RoundedCornerShape(Dimen.spacing_xs))
            ) {
                items(predictions) { prediction ->
                    PredictionItem(prediction) { selectedLocationName ->
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
            .padding(Dimen.spacing_xs)
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
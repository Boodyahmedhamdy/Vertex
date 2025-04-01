package com.iti.vertex.locationpicker.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.kotlin.awaitFindAutocompletePredictions
import com.google.android.libraries.places.compose.autocomplete.components.PlacesAutocompleteTextField
import com.google.android.libraries.places.compose.autocomplete.models.AutocompletePlace
import com.google.android.libraries.places.compose.autocomplete.models.toPlaceDetails
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.iti.vertex.R
import com.iti.vertex.locationpicker.vm.LocationPickerViewModel
import com.iti.vertex.utils.Result
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

private const val TAG = "LocationPickerScreen"
@Composable
fun LocationPickerScreen(
    viewModel: LocationPickerViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val locationState = viewModel.locationState.collectAsStateWithLifecycle()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val searchQueryState = viewModel.searchQueryState.collectAsStateWithLifecycle()
    val predictionsState = viewModel.predictionsState.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        viewModel.searchQueryState.debounce(1.seconds).collect {
            if(it.isNotBlank()) viewModel.fetchLocationPredictions(query = it)
        }
    }

    LocationPickerScreenContent(
        uiState = uiState.value,
        locationState = locationState.value,
        onMapClicked = { viewModel.updateLocationState(it) },
        searchQuery = searchQueryState.value,
        onStringQueryChanged = {
            viewModel.updateSearchQueryState(it)
        },
        modifier = modifier,
        onFabClicked = {
            viewModel.addSelectedLocationToFavorite()
            viewModel.setAsCurrentLocation()
            navController.popBackStack()
        },
        predictionsState = predictionsState.value,
        onLocationSelected = {
            Log.i(TAG, "LocationPickerScreen: $it")
            viewModel.fetchPlace(it)
        }
    )


}

@Composable
fun LocationPickerScreenContent(
    uiState: Result<out Unit>,
    locationState: LatLng,
    onMapClicked: (LatLng) -> Unit,
    searchQuery: String,
    onStringQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    onFabClicked: () -> Unit,
    predictionsState: List<AutocompletePrediction>,
    onLocationSelected: (AutocompletePlace) -> Unit
) {
    val scope = rememberCoroutineScope()
    val markerState = rememberUpdatedMarkerState(position = locationState)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(locationState, 10f)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            LocationSearchBar(
                value = searchQuery,
                onValueChange = onStringQueryChanged,
                modifier = Modifier.fillMaxWidth(),
                predictions = predictionsState,
                onLocationSelected = onLocationSelected
            )
                 },
        floatingActionButton = { Button(onClick = onFabClicked) { Icon(Icons.Outlined.Add, contentDescription = "add location to favorite") } },
        floatingActionButtonPosition = FabPosition.Center
    ) {innerPadding ->

        when(uiState) {
            Result.Loading -> { Box(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center) { CircularProgressIndicator() } }
            is Result.Error -> { Box(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center) { Text(text = uiState.message) } }
            is Result.Success -> {
                GoogleMap(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    cameraPositionState = cameraPositionState,
                    onMapClick = onMapClicked,
                    uiSettings = MapUiSettings(),
                    properties = MapProperties(isMyLocationEnabled = true),
                    onMapLoaded = {
                        scope.launch {
                            cameraPositionState.animate(CameraUpdateFactory.newLatLng(locationState))
                        }
                    }
                ) {
                    Marker(
                        state = markerState,
                        title = "Cairo",
                        snippet = "Cairo Snippet"
                    )
                }

                LaunchedEffect(locationState) {
                    cameraPositionState.animate(CameraUpdateFactory.newLatLng(locationState))
                }
            }
        }
    }

}

@Composable
fun LocationSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    predictions: List<AutocompletePrediction>,
    onLocationSelected: (AutocompletePlace) -> Unit,
    modifier: Modifier = Modifier
) {
    PlacesAutocompleteTextField(
        searchText = value,
        onQueryChanged = onValueChange,
        predictions = predictions.map { it.toPlaceDetails() },
        onSelected = onLocationSelected,
        modifier = modifier,
        placeHolderText = stringResource(R.string.search_for_places)
    )
}

@Preview
@Composable
private fun LocationPickerScreenPreview() {

}
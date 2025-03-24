package com.iti.vertex.home.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.iti.vertex.R
import com.iti.vertex.data.dtos.SimpleForecastItem
import com.iti.vertex.data.dtos.Weather
import com.iti.vertex.home.states.CurrentWeatherUiState
import com.iti.vertex.home.toWeatherIconUrl
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
@Composable
fun CurrentWeatherCard(
    state: CurrentWeatherUiState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            // image
            AsyncImage(
                model = state.weather.icon.toWeatherIconUrl(),
                contentDescription = state.weather.description,
                placeholder = painterResource(R.drawable.baseline_downloading_24),
                error = painterResource(R.drawable.baseline_broken_image_24),
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(150.dp)
            )

            // details
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f).fillMaxHeight()
            ) {

                Text(text = state.weather.description)

                Text(
                    text = "${state.mainData.temp}",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(text = state.name)

                // Date and time
                Text(
                    text = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                )
            }
        }
    }

}



@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun CurrentWeatherCardPreview() {

}
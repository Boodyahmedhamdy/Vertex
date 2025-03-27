package com.iti.vertex.home.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.iti.vertex.R

import com.iti.vertex.home.states.CurrentWeatherUiState
import com.iti.vertex.home.toWeatherIconUrl
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalGlideComposeApi::class)
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
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            // image
            GlideImage(
                model = state.weather.icon.toWeatherIconUrl(),
                contentDescription = state.weather.description,
                loading = placeholder(painter = painterResource(R.drawable.baseline_downloading_24)),
                failure = placeholder(painter = painterResource(R.drawable.baseline_broken_image_24)),
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(150.dp)
            )

            // details
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {

                // description
                Text(
                    text = state.weather.description,
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                // temperature
                Text(
                    text = "${state.mainData.temp.toInt()}",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold
                )

                // feels like
                Text(text = stringResource(R.string.feels_like, state.mainData.feelsLike.toInt()), fontWeight = FontWeight.Thin)

                // city name
                Text(text = state.name, modifier = Modifier.padding(8.dp))

                // Date and time
                Text(
                    text = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }

}



@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun CurrentWeatherCardPreview() {

}
package com.iti.vertex.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.iti.vertex.R
import com.iti.vertex.data.dtos.MainData
import com.iti.vertex.data.dtos.SimpleForecastItem
import com.iti.vertex.data.dtos.Weather
import com.iti.vertex.home.toWeatherIconUrl
import com.iti.vertex.utils.getStringResFromConditionCode

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ForecastSectionListItem(
    state: SimpleForecastItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            // date and time
            Text(text = state.dtTxt.split(" ").last(), modifier = Modifier.alpha(0.8f).padding(8.dp))

            // image
            GlideImage(
                model = state.weather.first().icon.toWeatherIconUrl(),
                contentDescription = state.weather.first().description,
                loading = placeholder(R.drawable.baseline_downloading_24),
                failure = placeholder(R.drawable.baseline_broken_image_24),
                modifier = Modifier.size(100.dp)
            )

            Text(
                text = stringResource(getStringResFromConditionCode(state.weather.first().id)),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // temperature
            Text(
                text = "${state.mainData.temp.toInt()}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Preview
@Composable
private fun ForecastSectionItemPreview() {
    val items: List<SimpleForecastItem> = listOf(
        SimpleForecastItem(dtTxt = "2025-03-24 15:00:00", mainData = MainData(temp = 33.1), weather = listOf(
            Weather(icon = "https://openweathermap.org/img/wn/04d@2x.png")
        )),
        SimpleForecastItem(dtTxt = "2025-03-24 15:00:00", mainData = MainData(temp = 33.1), weather = listOf(
            Weather(icon = "https://openweathermap.org/img/wn/04d@2x.png")
        )),
        SimpleForecastItem(dtTxt = "2025-03-24 15:00:00", mainData = MainData(temp = 33.1), weather = listOf(
            Weather(icon = "https://openweathermap.org/img/wn/04d@2x.png")
        )),
    )

    LazyRow(
        contentPadding = PaddingValues(8.dp)
    ) {
        items(items) {
            ForecastSectionListItem(it)
        }
    }
}
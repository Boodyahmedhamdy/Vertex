package com.iti.vertex.favorite.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.vertex.R
import com.iti.vertex.data.dtos.City
import com.iti.vertex.data.dtos.Coord
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity

@Composable
fun FavoriteLocationListItem(
    state: ForecastEntity,
    onItemClicked: (ForecastEntity) -> Unit,
    onDeleteItemClicked: (ForecastEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onItemClicked(state) },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // city name
            Text(text = state.city.name, style = MaterialTheme.typography.displaySmall)
            // country
            Text(text = stringResource(R.string.country, state.city.country), style = MaterialTheme.typography.bodyLarge)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = { onDeleteItemClicked(state) }) {
                    Icon(
                        Icons.Outlined.Delete,
                        contentDescription = stringResource(
                            R.string.delete_from_favorite_locations,
                            state.city.name
                        ),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun FavoriteLocationListItemPreview() {
    val items = listOf(
        ForecastEntity(city= City(coord= Coord(lat=32.973, lon=-80.8033), country="US", id=4600065, name="Walterboro", population=5398, sunrise=1742987916, sunset=1743032328, timezone=-14400)),
        ForecastEntity(city= City(coord= Coord(lat=32.973, lon=-80.8033), country="US", id=4600065, name="Walterboro", population=5398, sunrise=1742987916, sunset=1743032328, timezone=-14400)),
        ForecastEntity(city= City(coord= Coord(lat=32.973, lon=-80.8033), country="US", id=4600065, name="Walterboro", population=5398, sunrise=1742987916, sunset=1743032328, timezone=-14400)),
        ForecastEntity(city= City(coord= Coord(lat=32.973, lon=-80.8033), country="US", id=4600065, name="Walterboro", population=5398, sunrise=1742987916, sunset=1743032328, timezone=-14400)),
        ForecastEntity(city= City(coord= Coord(lat=32.973, lon=-80.8033), country="US", id=4600065, name="Walterboro", population=5398, sunrise=1742987916, sunset=1743032328, timezone=-14400)),
    )


    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(items) {
            FavoriteLocationListItem(
                state = it,
                onItemClicked = {  },
                onDeleteItemClicked = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }

    }
}

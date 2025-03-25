package com.iti.vertex.favorite

import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import kotlinx.coroutines.flow.SharedFlow

data class FavoriteScreenUiState(
    val isLoading: Boolean = false,
    val items: List<ForecastEntity> = listOf(),
    val notificationMessage: SharedFlow<String>
)

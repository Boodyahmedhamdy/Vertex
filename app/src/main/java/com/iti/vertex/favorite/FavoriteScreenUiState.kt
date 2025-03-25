package com.iti.vertex.favorite

import com.iti.vertex.data.sources.local.db.entities.ForecastEntity

data class FavoriteScreenUiState(
    val isLoading: Boolean = false,
    val items: List<ForecastEntity> = listOf(),
    val message: String = ""
)

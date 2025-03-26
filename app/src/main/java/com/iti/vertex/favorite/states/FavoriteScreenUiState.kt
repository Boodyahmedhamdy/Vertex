package com.iti.vertex.favorite.states

import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import kotlinx.coroutines.flow.SharedFlow

data class FavoriteScreenUiState(
    val isLoading: Boolean = false,
    val items: List<ForecastEntity> = listOf(),
    val isDeleteDialogVisible: Boolean = false,
    val notificationMessage: SharedFlow<String>
)

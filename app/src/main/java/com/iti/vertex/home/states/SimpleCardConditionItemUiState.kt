package com.iti.vertex.home.states

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.iti.vertex.R

data class SimpleCardConditionItemUiState(
    @DrawableRes val imgResId: Int = R.drawable.baseline_broken_image_24,
    val value: String = "DEFAULT VALUE",
    @StringRes val unit: Int = R.string.app_name,
    @StringRes val label: Int = R.string.app_name
)
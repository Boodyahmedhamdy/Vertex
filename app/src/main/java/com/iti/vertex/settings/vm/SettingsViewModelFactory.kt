package com.iti.vertex.settings.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.vertex.data.repos.settings.ISettingsRepository
import com.iti.vertex.data.repos.settings.SettingsRepository

@Suppress("UNCHECKED_CAST")
class SettingsViewModelFactory(
    private val repository: ISettingsRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(repository) as T
    }
}
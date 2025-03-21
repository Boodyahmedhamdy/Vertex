package com.iti.vertex.home.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.vertex.data.repos.IForecastRepository

class HomeViewModelFactory(
    private val repository: IForecastRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository = repository) as T
    }
}
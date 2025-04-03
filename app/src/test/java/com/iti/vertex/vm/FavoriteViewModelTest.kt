package com.iti.vertex.vm

import com.iti.vertex.data.dtos.City
import com.iti.vertex.data.dtos.Coord
import com.iti.vertex.data.repos.forecast.DummyForecastRemoteDataSource
import com.iti.vertex.data.repos.forecast.FakeForecastLocalDataSource
import com.iti.vertex.data.repos.forecast.ForecastRepository
import com.iti.vertex.data.repos.forecast.IForecastRepository
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import com.iti.vertex.data.sources.local.forecast.IForecastLocalDataSource
import com.iti.vertex.data.sources.remote.forecast.IForecastRemoteDataSource
import com.iti.vertex.favorite.vm.FavoriteViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class FavoriteViewModelTest {

    private lateinit var repository: IForecastRepository
    private lateinit var localDataSource: IForecastLocalDataSource
    private lateinit var remoteDataSource: IForecastRemoteDataSource
    private lateinit var viewModel: FavoriteViewModel
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
        remoteDataSource = DummyForecastRemoteDataSource()
        localDataSource = FakeForecastLocalDataSource()
        repository = ForecastRepository.getInstance(remoteDataSource, localDataSource)
        viewModel = FavoriteViewModel(repository as ForecastRepository)
    }

    @Test
    fun toggleShowDeleteLocationDialog() = runTest {
        // arrange
        // act
        viewModel.toggleShowDeleteLocationDialog()

        // assert
        val result = viewModel.showDeleteLocationDialog.value
        assertTrue(result)
    }

    @Test
    fun updateSelectedItemToBeDeleted() = runTest {

        //arrange
        val newItem = ForecastEntity(city = City(coord = Coord(lat = 1.1, lon = 1.1)))
        // act
        viewModel.updateSelectedItemToBeDeleted(newItem)

        // assert
        val result = viewModel.selectedItemToBeDeleted.value

        assertEquals(1.1, result.city.coord.lat, 0.0)
        assertEquals(1.1, result.city.coord.lon, 0.0)
    }

    @After
    fun finish() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }
}
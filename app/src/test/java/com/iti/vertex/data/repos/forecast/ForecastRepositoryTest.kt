package com.iti.vertex.data.repos.forecast

import com.iti.vertex.data.dtos.City
import com.iti.vertex.data.dtos.Coord
import com.iti.vertex.data.dtos.FullForecastResponse
import com.iti.vertex.data.dtos.current.CurrentWeatherResponse
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import com.iti.vertex.data.sources.local.forecast.IForecastLocalDataSource
import com.iti.vertex.data.sources.remote.forecast.IForecastRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test


class DummyForecastRemoteDataSource: IForecastRemoteDataSource {
    override suspend fun getFullForecast(lat: Double, long: Double): FullForecastResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentWeather(lat: Double, long: Double): CurrentWeatherResponse {
        TODO("Not yet implemented")
    }

}

class FakeForecastLocalDataSource(
    private val items: MutableList<ForecastEntity> = mutableListOf()
): IForecastLocalDataSource {
    override suspend fun insertForecast(forecastEntity: ForecastEntity) {
        items.add(forecastEntity)
    }

    override fun getAllForecast(): Flow<List<ForecastEntity>> {
        return listOf(items).asFlow()
    }

    override suspend fun getForecastByLatLong(lat: Double, long: Double): ForecastEntity {
        return items.find {
            it.city.coord.lat == lat && it.city.coord.lon == long
        } ?: ForecastEntity()
    }

    override suspend fun deleteForecast(entity: ForecastEntity) {
        items.remove(entity)
    }

}

class ForecastRepositoryTest {

    private lateinit var repository: ForecastRepository
    private lateinit var localDataSource: IForecastLocalDataSource
    private lateinit var remoteDataSource: IForecastRemoteDataSource


    @Before
    fun setup() {
        remoteDataSource = DummyForecastRemoteDataSource()
        localDataSource = FakeForecastLocalDataSource()
        repository = ForecastRepository.getInstance(remoteDataSource, localDataSource)
    }

    @Test
    fun getFavoriteForecastByLatLong_ReturnsInsertedForecast() = runBlocking {
        // arrange
        val firstEntity = ForecastEntity(city = City(coord = Coord(lat = 1.1, lon = 1.1)))
        val secondEntity = ForecastEntity(city = City(coord = Coord(lat = 2.2, lon = 2.2)))
        repository.addToFavorite(firstEntity)
        repository.addToFavorite(secondEntity)

        // act
        val result = repository.getFavoriteForecastByLatLong(lat = 1.1, long = 1.1)

        // assert
        assertEquals(1.1, result.city.coord.lat, 0.0)
        assertEquals(1.1, result.city.coord.lon, 0.0)
    }





    @Test
    fun deleteForecast_defaultObjectIsReturned() = runBlocking {
        // arrange
        val firstEntity = ForecastEntity(city = City(coord = Coord(lat = 1.1, lon = 1.1)))
        val secondEntity = ForecastEntity(city = City(coord = Coord(lat = 2.2, lon = 2.2)))
        repository.addToFavorite(firstEntity)
        repository.addToFavorite(secondEntity)

        // act
        repository.deleteForecast(firstEntity)

        // assert
        val result = repository.getFavoriteForecastByLatLong(1.1, 1.1)

        assertEquals(0.0, result.city.coord.lat, 0.0)
        assertEquals(0.0, result.city.coord.lon, 0.0)


    }
}
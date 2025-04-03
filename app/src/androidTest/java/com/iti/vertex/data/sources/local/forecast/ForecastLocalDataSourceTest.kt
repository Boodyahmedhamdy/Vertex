package com.iti.vertex.data.sources.local.forecast

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.iti.vertex.data.dtos.City
import com.iti.vertex.data.dtos.Coord
import com.iti.vertex.data.dtos.SimpleForecastItem
import com.iti.vertex.data.sources.local.db.ForecastDao
import com.iti.vertex.data.sources.local.db.VertexDatabase
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@MediumTest
@RunWith(AndroidJUnit4::class)
class ForecastLocalDataSourceTest {
    private lateinit var db: VertexDatabase
    private lateinit var dao: ForecastDao
    private lateinit var dataSource: ForecastLocalDataSource

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, VertexDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        dao = db.getForecastDao()
        dataSource = ForecastLocalDataSource(dao)
    }

    @Test
    fun insertForecast_ReturnTheInsertedEntity() = runTest {
        // arrange
        val city = City(coord = Coord(lat = 1.2, lon = 2.3), country = "Country", 1, name = "Name")
        val list: List<SimpleForecastItem> = emptyList()
        val entity = ForecastEntity(city = city, list = list)

        // act
        dataSource.insertForecast(entity)

        // assert
        val result = dataSource.getForecastByLatLong(lat = city.coord.lat, long = city.coord.lon)

        assertEquals(1.2, result.city.coord.lat, 0.0)
        assertEquals(2.3, result.city.coord.lon, 0.0)
        assertEquals("Country", result.city.country)
        assertEquals("Name", result.city.name)
        assertEquals(0, result.list.size)

    }

    @Test
    fun deleteForecast_theReturnedEntityIsNull() = runTest {
        // arrange
        val city = City(coord = Coord(lat = 1.2, lon = 2.3), country = "Country", 1, name = "Name")
        val list: List<SimpleForecastItem> = emptyList()
        val entity = ForecastEntity(city = city, list = list)
        dataSource.insertForecast(entity)


        // act
        dataSource.deleteForecast(entity)

        // assert
        val result = dataSource.getForecastByLatLong(lat = city.coord.lat, long = city.coord.lon)

        assertEquals(result, null)
    }


    @Test
    fun getAllForecast_ReturnedListHasSingleItem() = runTest {
        // arrange
        val entity = ForecastEntity()
        dataSource.insertForecast(entity)

        // act
        val result = dataSource.getAllForecast().first()

        // assert
        assertEquals(1, result.size)


    }



    @After
    fun finish() { db.close() }


}
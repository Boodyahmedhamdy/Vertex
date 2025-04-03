package com.iti.vertex.data.sources.local.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.iti.vertex.data.dtos.City
import com.iti.vertex.data.dtos.Coord
import com.iti.vertex.data.dtos.SimpleForecastItem
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@SmallTest
@RunWith(AndroidJUnit4::class)
class ForecastDaoTest {

    private lateinit var db: VertexDatabase
    private lateinit var dao: ForecastDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, VertexDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        dao = db.getForecastDao()
    }

    @Test
    fun insertForecast_retrievesInsertedForecast() = runTest {
        // arrange
        val city = City(coord = Coord(lat = 1.2, lon = 2.3), country = "Country", 1, name = "Name")
        val list: List<SimpleForecastItem> = emptyList()
        val entity = ForecastEntity(city = city, list = list)


        // act
        dao.insertForecast(entity)

        // assert
        val result = dao.getForecastByLatLong(lat = city.coord.lat, lon = city.coord.lon)

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
        dao.insertForecast(entity)


        // act
        dao.deleteForecast(entity)

        // assert
        val result = dao.getForecastByLatLong(lat = city.coord.lat, lon = city.coord.lon)

        assertEquals(result, null)
    }

    @Test
    fun getAllForecast_ReturnedListHasSingleItem() = runTest {
        // arrange
        val entity = ForecastEntity()
        dao.insertForecast(entity)

        // act
        val result = dao.getAllForecast().first()

        // assert
        assertEquals(1, result.size)


    }

    @After
    fun finish() { db.close() }

}
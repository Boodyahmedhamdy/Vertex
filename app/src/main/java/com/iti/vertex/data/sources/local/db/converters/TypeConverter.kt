package com.iti.vertex.data.sources.local.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iti.vertex.data.dtos.City
import com.iti.vertex.data.dtos.Clouds
import com.iti.vertex.data.dtos.Coord
import com.iti.vertex.data.dtos.DetailedWind
import com.iti.vertex.data.dtos.MainData
import com.iti.vertex.data.dtos.SimpleForecastItem
import com.iti.vertex.data.dtos.SimpleSys
import com.iti.vertex.data.dtos.Weather

class TypeConverter {

    private val gson = Gson()

    // ----------------- City -----------------------
    @TypeConverter
    fun fromJsonToCity(json: String): City {
        return gson.fromJson(json, City::class.java)
    }

    @TypeConverter
    fun fromCityToJson(city: City): String {
        return gson.toJson(city)
    }


    // ----------------- List<SimpleForecastItem> -----------------------
    @TypeConverter
    fun fromListOfSimpleForecastItemToJson(list: List<SimpleForecastItem>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromJsonToListOfSimpleForecastItem(json: String): List<SimpleForecastItem> {
        val type = object : TypeToken<List<SimpleForecastItem>>() {}.type
        return gson.fromJson(json, type)
    }


    // ------------- Coord ------------------
    @TypeConverter
    fun fromCoordToJson(coord: Coord): String {
        return gson.toJson(coord)
    }

    @TypeConverter
    fun fromJsonToCoord(json: String): Coord {
        return gson.fromJson(json, Coord::class.java)
    }

    // ------------- Clouds ------------------
    @TypeConverter
    fun fromCloudsToJson(clouds: Clouds): String = gson.toJson(clouds)
    @TypeConverter
    fun fromJsonToClouds(json: String): Clouds = gson.fromJson(json, Clouds::class.java)

    // ------------- MainData ------------------
    @TypeConverter
    fun fromMainDataToJson(mainData: MainData): String = gson.toJson(mainData)
    @TypeConverter
    fun fromJsonToMainData(json: String): MainData = gson.fromJson(json, MainData::class.java)


    // ---------------------- SimpleSys -------------------
    @TypeConverter
    fun formSimpleSysToJson(simpleSys: SimpleSys): String = gson.toJson(simpleSys)

    @TypeConverter
    fun fromJsonToSimpleSys(json: String): SimpleSys = gson.fromJson(json, SimpleSys::class.java)


    // ---------------------- DetailedWind -------------------
    @TypeConverter
    fun formDetailedToJson(detailedWind: DetailedWind): String = gson.toJson(detailedWind)

    @TypeConverter
    fun fromJsonToDetailedWind(json: String): DetailedWind = gson.fromJson(json, DetailedWind::class.java)



    // ---------------------- DetailedWind -------------------
    @TypeConverter
    fun fromWeatherToJson(weather: Weather): String = gson.toJson(weather)
    @TypeConverter
    fun fromJsonToWeather(json: String): Weather = gson.fromJson(json, Weather::class.java)


    // ----------------- List<Weather> -----------------------
    @TypeConverter
    fun fromListOfWeatherToJson(list: List<Weather>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromJsonToListOfWeather(json: String): List<Weather> {
        val type = object : TypeToken<List<Weather>>() {}.type
        return gson.fromJson(json, type)
    }



}
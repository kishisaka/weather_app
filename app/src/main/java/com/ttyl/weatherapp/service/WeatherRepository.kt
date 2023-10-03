package com.ttyl.weatherapp.service

import com.ttyl.weatherapp.domain.Location
import com.ttyl.weatherapp.domain.WeatherShort
import retrofit2.Call

class WeatherRepository(private val weatherService: WeatherService?) {

    fun getWeather(lat: String, long: String, key: String,
                   units: String = "imperial"): Call<WeatherShort>? {
        return weatherService?.getWeather(lat, long, key, units)
    }

    fun getLocation(query: String, key: String, limit: String = "10"): Call<Array<Location>>? {
        return weatherService?.getLocation(query, key, limit)
    }
}
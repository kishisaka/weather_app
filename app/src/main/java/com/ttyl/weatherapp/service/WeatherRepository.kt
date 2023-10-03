package com.ttyl.weatherapp.service

import com.ttyl.weatherapp.domain.Location
import com.ttyl.weatherapp.domain.WeatherShort
import retrofit2.Call

/**
 * We can add ROOM persistence later and store all the saved weather data with a 12 hour
 * expiration here. Right now we only store the last searched lat and long in
 * shared prefs.
 */
class WeatherRepository(private val weatherService: WeatherService?) {

    fun getWeather(lat: String, long: String, key: String,
                   units: String = "imperial"): Call<WeatherShort>? {
        return weatherService?.getWeather(lat, long, key, units)
    }

    fun getLocation(query: String, key: String, limit: String = "10"): Call<Array<Location>>? {
        return weatherService?.getLocation(query, key, limit)
    }
}
package com.ttyl.weatherapp.service

import com.ttyl.weatherapp.domain.Location
import com.ttyl.weatherapp.domain.WeatherShort
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * add geo reverse lookup so we can get the city, state and country information later from the
 * lat and long.
 */
interface WeatherService {
    @GET("/geo/1.0/direct")
    fun getLocation(@Query("q") q: String, @Query("appid") key: String,
                    @Query("limit") limit: String): Call<Array<Location>>

    @GET("/data/2.5/weather")
    fun getWeather(@Query("lat") lat: String, @Query("lon") long: String,
                   @Query("appid") appid: String, @Query("units") units: String): Call<WeatherShort>
}
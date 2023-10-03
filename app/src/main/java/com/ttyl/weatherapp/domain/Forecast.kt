package com.ttyl.weatherapp.domain

import com.squareup.moshi.Json

data class Forecast(
    @field: Json(name="cod") val cod: String,
    @field: Json(name="message") val message: Int,
    @field: Json(name="cnt") val cnt: Int,
    @field: Json(name="list") val list: List<WeatherItem>,
    @field: Json(name="city") val city: City,
)
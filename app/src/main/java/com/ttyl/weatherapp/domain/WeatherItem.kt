package com.ttyl.weatherapp.domain

import com.squareup.moshi.Json

data class WeatherItem(
    @field: Json(name="dt") val dt: Long,
    @field: Json(name="main") val main: Main,
    @field: Json(name="weather") val weather: List<Weather>,
    @field: Json(name="clouds") val cloud: Clouds,
    @field: Json(name="wind") val wind: Wind,
    @field: Json(name="visibility") val visibility: Int,
    @field: Json(name="pop") val pop: Float,
    @field: Json(name="sys") val sys: Sys,
    @field: Json(name="dt_txt") val dtTxt: String
)
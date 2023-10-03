package com.ttyl.weatherapp.domain

import com.squareup.moshi.Json

data class Main(
    @field: Json(name="temp") val temp: Float,
    @field: Json(name="feels_like") val feelsLike: Float,
    @field: Json(name="temp_min") val tempMin: Float,
    @field: Json(name="temp_max") val tempMax: Float,
    @field: Json(name="pressure") val pressure: Int,
    @field: Json(name="sea_level") val seaLevel: Int,
    @field: Json(name="grnd_level") val groundLevel: Int,
    @field: Json(name="humidity") val humidity: Int,
    @field: Json(name="temp_kf") val tempKf: Float
)
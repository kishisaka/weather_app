package com.ttyl.weatherapp.domain

import com.squareup.moshi.Json

data class City(
    @field: Json(name="id") val id: Long,
    @field: Json(name="name") val name: String,
    @field: Json(name="coord") val coordinates: Coordinates,
    @field: Json(name="country") val country: String,
    @field: Json(name="population") val population: Long,
    @field: Json(name="timezone") val timezone: Long,
    @field: Json(name="sunrise") val sunrise: Long,
    @field: Json(name="sunset") val sunset: Long
)
package com.ttyl.weatherapp.domain

import com.squareup.moshi.Json

data class Coordinates(
    @field: Json(name="lat") val lat: String,
    @field: Json(name="lon") val long: String
)
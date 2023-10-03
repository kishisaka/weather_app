package com.ttyl.weatherapp.domain

import com.squareup.moshi.Json

data class Clouds(
    @field: Json(name="all") val all: Int
)
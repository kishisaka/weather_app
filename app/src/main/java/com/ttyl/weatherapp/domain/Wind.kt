package com.ttyl.weatherapp.domain

import com.squareup.moshi.Json

data class Wind (
    @field: Json(name="speed") val speed: Float,
    @field: Json(name="deg") val deg: Float,
    @field: Json(name="gust") val gust: Float
)
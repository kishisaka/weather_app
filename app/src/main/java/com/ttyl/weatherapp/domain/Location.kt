package com.ttyl.weatherapp.domain

import com.squareup.moshi.Json

data class Location(
    @field: Json(name="name") val name: String,
    @field: Json(name="local_names") val localNames: LocalNames,
    @field: Json(name="lat") val lat: String,
    @field: Json(name="lon") val long: String,
    @field: Json(name="country") val country: String,
    @field: Json(name="state") val state: String
)
package com.ttyl.weatherapp

import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class WeatherPreferences(private val sharedPreferences: SharedPreferences,
                         private val io: CoroutineContext,
                         private val main: CoroutineContext) {

    fun saveCurrentSearch(lat: String, long: String) {
        CoroutineScope(io).launch {
            val editor = sharedPreferences.edit()
            editor.putString("lat", lat)
            editor.putString("long", long)
            editor.apply()
        }
    }

    fun getLastSearch(getLastSearchCallback: GetLastSearchCallback) {
        CoroutineScope(io).launch {
            var lat = "NONE"
            sharedPreferences.getString("lat", "NONE")?.let {
                lat = it
            }
            var long = "NONE"
            sharedPreferences.getString("long", "NONE")?.let {
                long = it
            }
            withContext(main) {
                getLastSearchCallback.onSuccess(lat, long)
            }
        }
    }
}

interface GetLastSearchCallback {
    fun onSuccess(lat: String, long: String)
}
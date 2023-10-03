package com.ttyl.weatherapp

import android.app.Activity

class WeatherBusiness(val activity: Activity) {

    /**
     * put all the business logic here, inject all the dependencies here as well.
     */

    companion object {
        const val WEATHER_API_KEY = "<your open weather map API key goes here>"
        const val REQUEST = 1000
        const val SHARED_PREF_NAME = "weather"
    }

    fun getWeatherForCurrentLocation(weatherPreferences: WeatherPreferences,
                                     locationProvider: LocationProvider,
                                     locationCallback: LocationCallback,
                                     viewModel: WeatherViewModel) {
        weatherPreferences.getLastSearch(object: GetLastSearchCallback {
            override fun onSuccess(lat: String, long: String) {
                if (lat == ("NONE") || (long == ("NONE"))) {
                    locationProvider.getCurrentLocation(locationCallback, activity)
                } else {
                    viewModel.getWeatherShort(
                        lat, long, WEATHER_API_KEY)
                }
            }
        })
    }
}
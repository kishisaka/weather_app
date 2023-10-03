package com.ttyl.weatherapp

import android.app.Activity
import android.content.SharedPreferences
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Test

class WeatherActivityTests {

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @Test
    @ExperimentalCoroutinesApi
    fun getWeatherForCurrentLocationTest_1_2() {
        val activity = mockk<Activity>()
        val sharedPreferences = mockk<SharedPreferences>()
        every { sharedPreferences.getString("lat", "NONE") } returns "1"
        every { sharedPreferences.getString("long", "NONE") } returns "2"
        val weatherPreferences = WeatherPreferences(sharedPreferences, testDispatcher, testDispatcher)
        val getLastSearchCallback = mockk<GetLastSearchCallback>()
        every {getLastSearchCallback.onSuccess("1", "2")} just runs
        every {weatherPreferences.getLastSearch(getLastSearchCallback)} just runs
        val locationProvider = mockk<LocationProvider>()
        val locationCallback = mockk<LocationCallback>()
        val viewModel = mockk<WeatherViewModel>()
        val weatherBusiness = WeatherBusiness(activity)
        weatherBusiness.getWeatherForCurrentLocation(weatherPreferences, locationProvider, locationCallback, viewModel)
        verify { viewModel.getWeatherShort("1", "2", any()) }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun getWeatherForCurrentLocationTest_NONE_NONE() {
        val activity = mockk<Activity>()
        val sharedPreferences = mockk<SharedPreferences>()
        every { sharedPreferences.getString("lat", "NONE") } returns "NONE"
        every { sharedPreferences.getString("long", "NONE") } returns "NONE"
        val weatherPreferences = WeatherPreferences(sharedPreferences, testDispatcher, testDispatcher)
        val getLastSearchCallback = mockk<GetLastSearchCallback>()
        every {getLastSearchCallback.onSuccess("NONE", "NONE")} just runs
        every {weatherPreferences.getLastSearch(getLastSearchCallback)} just runs
        val locationProvider = mockk<LocationProvider>()
        val locationCallback = mockk<LocationCallback>()
        every {locationProvider.getCurrentLocation(locationCallback, activity)} just runs
        every {locationCallback.onSuccess(any())} just runs
        val viewModel = mockk<WeatherViewModel>()
        val weatherBusiness = WeatherBusiness(activity)
        weatherBusiness.getWeatherForCurrentLocation(weatherPreferences, locationProvider, locationCallback, viewModel)
        verify { locationProvider.getCurrentLocation(any(), any()) }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun weatherPreferencesTest_NONE_NONE() {
        val sharedPreferences = mockk<SharedPreferences>()
        every { sharedPreferences.getString("lat", "NONE") } returns "NONE"
        every { sharedPreferences.getString("long", "NONE") } returns "NONE"
        val weatherPreferences = WeatherPreferences(sharedPreferences, testDispatcher, testDispatcher)
        val getLastSearchCallback = mockk<GetLastSearchCallback>()
        every {getLastSearchCallback.onSuccess("NONE", "NONE")} just runs
        weatherPreferences.getLastSearch(getLastSearchCallback)
        verify { getLastSearchCallback.onSuccess("NONE", "NONE") }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun weatherPreferencesTest_1_2() {
        val sharedPreferences = mockk<SharedPreferences>()
        every { sharedPreferences.getString("lat", "NONE") } returns "1"
        every { sharedPreferences.getString("long", "NONE") } returns "2"
        val weatherPreferences = WeatherPreferences(sharedPreferences, testDispatcher, testDispatcher)
        val getLastSearchCallback = mockk<GetLastSearchCallback>()
        every {getLastSearchCallback.onSuccess("1", "2")} just runs
        weatherPreferences.getLastSearch(getLastSearchCallback)
        verify { getLastSearchCallback.onSuccess("1", "2") }
    }
}
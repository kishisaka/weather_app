package com.ttyl.weatherapp

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ttyl.weatherapp.databinding.ActivityMainBinding
import com.ttyl.weatherapp.domain.WeatherShort
import com.ttyl.weatherapp.service.WeatherRepository
import com.ttyl.weatherapp.service.WeatherClient
import kotlinx.coroutines.Dispatchers

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherPreferences: WeatherPreferences
    private lateinit var weatherBusiness: WeatherBusiness

    private val viewModel: WeatherViewModel by lazy {
        ViewModelProvider(this)[(WeatherViewModel::class.java)]
    }

    private val locationCallback = object: LocationCallback {
        override fun onSuccess(location: Location?) {
            weatherPreferences.saveCurrentSearch(location?.latitude.toString(), location?.longitude.toString())
            viewModel.getWeatherShort(
                location?.latitude.toString(),
                location?.longitude.toString(),
                WeatherBusiness.WEATHER_API_KEY
            )
        }

        override fun onFail() {
            showFailure()
        }
    }

    private fun showFailure() {
        Snackbar.make(binding.root, getString(R.string.server_failure_no_location), Snackbar.LENGTH_LONG).show()
        binding.temperature.text = getString(R.string.n_a)
        binding.feelsLike.text =  getString(R.string.n_a)
        binding.windSpeed.text =  getString(R.string.n_a)
        binding.windDirection.text =  getString(R.string.n_a)
        binding.location.text =  getString(R.string.n_a)
        binding.weatherDescription.text =  getString(R.string.n_a)
    }

    private fun showWeather(weather: WeatherShort) {
        binding.temperature.text = getString(R.string.temp).format(weather.main.temp)
        binding.feelsLike.text = getString(R.string.temp).format(weather.main.feelsLike)
        binding.windSpeed.text = getString(R.string.wind).format(weather.wind.speed)
        binding.windDirection.text = getString(R.string.gust).format(weather.wind.gust)
        binding.location.text = weather.name
        binding.weatherDescription.text = getString(R.string.weather_description).format(weather.weather[0].description, weather.wind.deg, weather.wind.speed, weather.wind.gust)
        Glide.with(this).load("https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png").into(binding.weatherIcon)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            WeatherBusiness.REQUEST -> {
                 weatherBusiness.getWeatherForCurrentLocation(weatherPreferences,
                     LocationProvider(), locationCallback, viewModel)
            }
            else -> {
                // do nothing!
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.weatherSearchButton.setOnClickListener {
            createWeatherSearchDialog()
        }

        // inject these using DI framework (dagger hilt/toothpick/koin) later
        // move more of the business logic into the WeatherBusiness later!
        // Pass an interface with the showFailure() and showWeather()
        // implementations in to populate fields from WeatherBusiness
        viewModel.setWeatherRepository(WeatherRepository(WeatherClient.getInstance()))
        weatherPreferences = WeatherPreferences(
            getSharedPreferences(WeatherBusiness.SHARED_PREF_NAME, MODE_PRIVATE), Dispatchers.IO, Dispatchers.Main)
        weatherBusiness = WeatherBusiness(this)

        // observe changes when location API is called
        viewModel.locationLiveData.observe(this) { location ->
            when (location) {
                is ScreenState.Success -> {
                    location.data?.let {
                        // save current search location
                        if (it.isNotEmpty()) {
                            weatherPreferences.saveCurrentSearch(it[0].lat, it[0].long)
                            viewModel.getWeatherShort(
                                it[0].lat,
                                it[0].long,
                                WeatherBusiness.WEATHER_API_KEY
                            )
                        } else {
                            Snackbar.make(binding.root, getString(R.string.error_search), Snackbar.LENGTH_LONG).show()
                        }
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                }
                is ScreenState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ScreenState.Error -> {
                    showFailure()
                    binding.progressBar.visibility = View.INVISIBLE
                }
                else -> {
                    showFailure()
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
        }

        // observe changes when weather short API is called
        viewModel.weatherShortLiveData.observe(this) { weatherShort ->
            when (weatherShort) {
                is ScreenState.Success -> {
                    binding.progressBar.visibility = ProgressBar.INVISIBLE
                    weatherShort.data?.let {
                        showWeather(it)
                    }
                }
                is ScreenState.Loading -> {
                    binding.progressBar.visibility = ProgressBar.VISIBLE
                }
                is ScreenState.Error -> {
                    showFailure()
                    binding.progressBar.visibility = ProgressBar.INVISIBLE
                }
                else -> {
                    showFailure()
                    binding.progressBar.visibility = ProgressBar.INVISIBLE
                }
            }
        }

        // check GPS location permissions, if none, request it from user, then get
        // current location and get the current weather for that location
        // show N/A and let the user know why they don't see data if permissions are
        // not granted.
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), WeatherBusiness.REQUEST)
        } else {
            weatherBusiness.getWeatherForCurrentLocation(weatherPreferences,
                LocationProvider(), locationCallback, viewModel)
        }
    }

    private fun createWeatherSearchDialog() {
        // use this button to search for a location. lock searches to US only by concatenating
        // a "usa" to the query.
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.weather_search_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(true)
        val searchButton = dialog.findViewById<Button>(R.id.search_button)
        val searchFieldLayout = dialog.findViewById<TextInputLayout>(R.id.city_search_field_layout)
        val searchField = searchFieldLayout.findViewById<TextInputEditText>(R.id.city_search_field)
        searchButton.setOnClickListener {
            val query = searchField.text?.toString()
            query?.let {
                if (query.isNotEmpty()) {
                    viewModel.getLocation("$query,usa", WeatherBusiness.WEATHER_API_KEY)
                } else {
                    Snackbar.make(binding.root, getString(R.string.empty_search_error), Snackbar.LENGTH_LONG).show()
                }
            }
            dialog.dismiss()
        }
        dialog.show()
    }
}
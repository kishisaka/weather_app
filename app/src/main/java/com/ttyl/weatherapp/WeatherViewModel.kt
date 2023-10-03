package com.ttyl.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ttyl.weatherapp.domain.Location
import com.ttyl.weatherapp.domain.WeatherShort
import com.ttyl.weatherapp.service.WeatherRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel: ViewModel() {

    private var weatherRepository: WeatherRepository? = null

    private val _weatherShortLiveData = MutableLiveData<ScreenState<WeatherShort>>()
    val weatherShortLiveData: LiveData<ScreenState<WeatherShort>?>
        get() = _weatherShortLiveData

    private val _locationLiveData = MutableLiveData<ScreenState<Array<Location>>>()
    val locationLiveData: LiveData<ScreenState<Array<Location>>?>
        get() = _locationLiveData

    fun setWeatherRepository(weatherRepository: WeatherRepository)  {
        this.weatherRepository = weatherRepository
    }

    fun getWeatherShort(lat: String, long: String, key: String) {
        val callback = object: Callback<WeatherShort> {
            override fun onResponse(call: Call<WeatherShort>, response: Response<WeatherShort>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherShortLiveData.postValue(ScreenState.Success(it))
                    }
                } else {
                    _weatherShortLiveData.postValue(ScreenState.Error(response.message()))
                }
            }

            override fun onFailure(call: Call<WeatherShort>, t: Throwable) {
                t.message?.let {
                    _weatherShortLiveData.postValue(ScreenState.Error(it))
                }
            }
        }
        val call = weatherRepository?.getWeather(lat, long, key)
        _weatherShortLiveData.postValue(ScreenState.Loading(null))
        call?.enqueue(callback)
    }

    fun getLocation(query: String, key: String) {
        val callback = object: Callback<Array<Location>> {
            override fun onResponse(call: Call<Array<Location>>, response: Response<Array<Location>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _locationLiveData.postValue(ScreenState.Success(it))
                    }
                } else {
                    _locationLiveData.postValue(ScreenState.Error(response.message()))
                }
            }

            override fun onFailure(call: Call<Array<Location>>, t: Throwable) {
                t.message?.let {
                    _locationLiveData.postValue(ScreenState.Error(it))
                }
            }
        }
        val call = weatherRepository?.getLocation(query, key)
        _locationLiveData.postValue(ScreenState.Loading(null))
        call?.enqueue(callback)
    }

}
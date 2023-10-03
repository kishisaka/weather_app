package com.ttyl.weatherapp.service

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object WeatherClient {

    private const val COUNTRY_ROOT_URL = "https://api.openweathermap.org/"
    private const val HTTP_OK = 200

    private var weatherService: WeatherService

    init {
        val client = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)

                // log errors to whatever observability platform you have
                if (response.code() != HTTP_OK) {
                    Log.e("Weather request", "${request.method()} " +
                            "${request.url()}, ${response.code()}, ${response.body()}"
                    )
                }
                response
            }
            .build()

        weatherService = Retrofit.Builder()
            .baseUrl(COUNTRY_ROOT_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(WeatherService::class.java)
    }

    fun getInstance(): WeatherService = weatherService
}
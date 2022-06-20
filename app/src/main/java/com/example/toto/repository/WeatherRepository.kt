package com.example.toto.repository

import com.example.toto.model.ForecastModel
import com.example.toto.model.WeatherModel
import com.example.toto.weather.RemoteDataSource
import com.example.toto.weather.RemoteDataSourceImpl
import org.json.JSONObject
import retrofit2.Response

class WeatherRepository {

    private val retrofitRemoteDataSource: RemoteDataSource = RemoteDataSourceImpl()
    fun getWeatherInfo(
        jsonObject: JSONObject,
        onResponse: (Response<WeatherModel>) -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        retrofitRemoteDataSource.getWeatherInfo(jsonObject, onResponse, onFailure)
    }

    fun getForecastInfo(
        jsonObject: JSONObject,
        onResponse: (Response<ForecastModel>) -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        retrofitRemoteDataSource.getForecastInfo(jsonObject, onResponse, onFailure)
    }

}
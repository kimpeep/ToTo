package com.example.toto.weather

import com.example.toto.model.ForecastModel
import com.example.toto.model.WeatherModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSourceImpl : RemoteDataSource {

    override fun getWeatherInfo(
        jsonObject: JSONObject,
        onResponse: (Response<WeatherModel>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val APIService: WeatherAPIService = WeatherAPIClient.getClient(jsonObject.get("url").toString()).create(WeatherAPIService::class.java)
        APIService.doGetJsonDataWeather(
            jsonObject.get("path").toString(),
            jsonObject.get("q").toString(),
            jsonObject.get("appid").toString()
        ).enqueue(object :
            Callback<WeatherModel> {
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                onResponse(response)
            }
            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                onFailure(t)
            }
        }
        )
    }

    override fun getForecastInfo(
        jsonObject: JSONObject,
        onResponse: (Response<ForecastModel>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val APIService: WeatherAPIService = WeatherAPIClient.getClient(jsonObject.get("url").toString()).create(WeatherAPIService::class.java)
        APIService.doGetJsonDataForecast(
            jsonObject.get("path").toString(),
            jsonObject.get("id").toString(),
            jsonObject.get("appid").toString()
        ).enqueue(object :
            Callback<ForecastModel> {
            override fun onResponse(call: Call<ForecastModel>, response: Response<ForecastModel>) {
                onResponse(response)
            }
            override fun onFailure(call: Call<ForecastModel>, t: Throwable) {
                onFailure(t)
            }
        }
        )
    }

}
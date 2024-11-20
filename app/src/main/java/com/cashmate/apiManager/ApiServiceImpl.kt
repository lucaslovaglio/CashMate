package com.cashmate.apiManager

import com.cashmate.common.BlueRateResponse
import retrofit.GsonConverterFactory
import retrofit.Retrofit


class ApiServiceImpl : ApiService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.bluelytics.com.ar/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    override suspend fun getExchangeRates(): BlueRateResponse {
        return apiService.getExchangeRates()
    }

    suspend fun getBlueRate(): Double {
        return apiService.getExchangeRates().blue.value_avg
    }
}

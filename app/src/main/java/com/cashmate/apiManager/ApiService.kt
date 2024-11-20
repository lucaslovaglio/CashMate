package com.cashmate.apiManager

import com.cashmate.common.BlueRateResponse
import retrofit.Call
import retrofit.http.GET

interface ApiService {
    @GET("v2/latest")
    suspend fun getExchangeRates(): BlueRateResponse
}
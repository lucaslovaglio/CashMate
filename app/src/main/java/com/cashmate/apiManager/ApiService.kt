package com.cashmate.apiManager

import com.cashmate.common.BlueRateResponse
import retrofit.Call
import retrofit.http.GET

interface ApiService {
    @GET("v2/latest")
    fun getExchangeRates(): Call<BlueRateResponse>
}
package com.cashmate.apiManager

import android.content.Context
import com.cashmate.common.BlueRateResponse
import android.widget.Toast
import retrofit.Call
import retrofit.Callback
import retrofit.GsonConverterFactory
import retrofit.Response
import retrofit.Retrofit
import javax.inject.Inject



class ApiServiceImpl @Inject constructor() {

    fun getExchangeRates(context: Context,onSuccess: (BlueRateResponse) -> Unit, onFail: () -> Unit, loadingFinished: () -> Unit) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(
                "https://api.bluelytics.com.ar/"
            )
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

        val service: ApiService = retrofit.create(ApiService::class.java)
        val call: Call<BlueRateResponse> = service.getExchangeRates()

        call.enqueue(object : Callback<BlueRateResponse> {
            override fun onResponse(response: Response<BlueRateResponse>?, retrofit: Retrofit?) {
                loadingFinished()
                if(response?.isSuccess == true) {
                    val exchangeRates: BlueRateResponse = response.body()
                    onSuccess(exchangeRates)
                } else {
                    onFailure(Exception("Bad request"))
                }
            }
            override fun onFailure(t: Throwable?) {
                Toast.makeText(context, "Can't get exchange rates", Toast.LENGTH_SHORT).show()
                onFail()
                loadingFinished()
            }
        })


    }
}

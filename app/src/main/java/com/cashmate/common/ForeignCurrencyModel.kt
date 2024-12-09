package com.cashmate.common

data class BlueRateResponse(
    val oficial: ExchangeRate,
    val blue: ExchangeRate,
    val oficial_euro: ExchangeRate,
    val blue_euro: ExchangeRate,
    val last_update: String
)

data class ExchangeRate(
    val value_avg: Double,
    val value_sell: Double,
    val value_buy: Double
)

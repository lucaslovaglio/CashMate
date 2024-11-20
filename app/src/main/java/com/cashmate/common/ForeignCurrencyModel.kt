package com.cashmate.common

data class BlueRateResponse(
    val blue: BlueDetails
)

data class BlueDetails(
    val value_avg: Double
)

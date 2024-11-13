package com.cashmate.logs

data class Movement(
    val memberName: String,
    val amount: Double,
    val date: String,
    val description: String
)

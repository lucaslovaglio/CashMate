package com.cashmate.stats

data class Transaction(
    val fromMemberId: Int,
    val toMemberId: Int,
    val amount: Double,
    val payerName: String,
    val receiverName: String
)

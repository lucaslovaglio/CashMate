package com.cashmate.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "trip")
data class Trip(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val date: String
)

@Entity(tableName = "members")
data class Member(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
)

@Entity(tableName = "expenses",
    foreignKeys = [ForeignKey(entity = Member::class,
        parentColumns = ["id"],
        childColumns = ["memberId"],
        onDelete = ForeignKey.CASCADE)])
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val memberId: Int,
    val amount: Double,
    val description: String,
    val date: String = Date().toString()
)

data class MemberWithExpense(
    val id: Int,
    val name: String,
    val totalSpent: Double
)

data class Transaction(
    val fromMemberId: Int,
    val toMemberId: Int,
    val amount: Double,
    val payerName: String,
    val receiverName: String
)

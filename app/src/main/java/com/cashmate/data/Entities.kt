package com.cashmate.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "trip")
data class Trip(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val date: String
)

@Entity(tableName = "trip_member",
    foreignKeys = [ForeignKey(entity = Trip::class,
        parentColumns = ["id"],
        childColumns = ["tripId"],
        onDelete = ForeignKey.CASCADE)])
data class TripMember(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val tripId: Int,           // Relación con la tabla Trip
    val memberId: Int,        // Relación con la tabla Member
    val spent: Double
)

@Entity(tableName = "members")
data class Member(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val spent: Double,
    val balance: Double
)

package com.cashmate.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TripDao {
    @Insert
    suspend fun insertTrip(trip: Trip)

    @Update
    suspend fun updateTrip(trip: Trip)

    @Query("SELECT * FROM trip WHERE id = :tripId")
    suspend fun getTripById(tripId: Int): Trip?

    @Query("SELECT * FROM trip")
    fun getAllTrips(): LiveData<List<Trip>>

    @Query("DELETE FROM trip WHERE id = :tripId")
    suspend fun deleteTrip(tripId: Int)
}


@Dao
interface TripMemberDao {
    @Insert
    suspend fun insertTripMember(tripMember: TripMember)

    @Update
    suspend fun updateTripMember(tripMember: TripMember)

    @Query("SELECT * FROM trip_member WHERE tripId = :tripId")
    suspend fun getMembersByTripId(tripId: Int): List<TripMember>

    @Query("SELECT * FROM trip_member WHERE tripId = :tripId AND memberId = :memberId")
    suspend fun getMemberByTripAndId(tripId: Int, memberId: Int): TripMember?

    @Query("UPDATE trip_member SET spent = spent + :amount WHERE tripId = :tripId AND memberId = :memberId")
    suspend fun addExpenseToMember(tripId: Int, memberId: Int, amount: Double)
}


@Dao
interface MemberDao {
    @Insert
    suspend fun insertMember(member: Member)

    @Update
    suspend fun updateMember(member: Member)

    @Query("SELECT * FROM members")
    fun getAllMembers(): LiveData<List<Member>>

    @Query("SELECT * FROM members WHERE id = :memberId")
    suspend fun getMemberById(memberId: Int): Member?

    @Query("SELECT * FROM members WHERE name = :name")
    suspend fun getMemberByName(name: String): Member?

    @Query("UPDATE members SET spent = spent + :amount WHERE id = :memberId")
    suspend fun addExpenseToMember(memberId: Int, amount: Double)

    @Query("SELECT balance FROM members WHERE id = :memberId")
    suspend fun getBalanceByMemberId(memberId: Int): Double

    @Query("SELECT SUM(balance) FROM members")
    suspend fun getTotalBalance(): Double
}
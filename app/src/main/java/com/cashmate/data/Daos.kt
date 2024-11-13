package com.cashmate.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


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
package com.cashmate.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface MemberDao {
    @Insert
    fun insertMember(member: Member)

    @Update
    fun updateMember(member: Member)

    @Query("SELECT * FROM members")
    fun getAllMembers(): LiveData<List<Member>>

    @Query("SELECT * FROM members WHERE id = :memberId")
    fun getMemberById(memberId: Int): Member?

    @Query("SELECT * FROM members WHERE name = :name")
    fun getMemberByName(name: String): Member?

    @Query("UPDATE members SET spent = spent + :amount WHERE id = :memberId")
    fun addExpenseToMember(memberId: Int, amount: Double)

    @Query("SELECT balance FROM members WHERE id = :memberId")
    fun getBalanceByMemberId(memberId: Int): Double

    @Query("SELECT SUM(balance) FROM members")
    fun getTotalBalance(): Double
}
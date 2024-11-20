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

    @Query("DELETE FROM members WHERE id = :memberId")
    fun deleteMember(memberId: Int)

    @Query("SELECT * FROM members")
    fun getAllMembers(): LiveData<List<Member>>

    @Query("SELECT * FROM members WHERE id = :memberId")
    suspend fun getMemberById(memberId: Int): Member?

    @Query("SELECT * FROM members WHERE name = :name")
    fun getMemberByName(name: String): Member?

    @Query("""
    SELECT m.*, COALESCE(SUM(e.amount), 0) as totalSpent
    FROM members m
    LEFT JOIN expenses e ON m.id = e.memberId
    GROUP BY m.id
""")
    fun getMembersWithTotalSpent(): LiveData<List<MemberWithExpense>>

}

@Dao
interface ExpenseDao {
    @Insert
    fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE memberId = :memberId")
    fun getExpensesByMemberId(memberId: Int): List<Expense>

    @Query("SELECT * FROM expenses WHERE id = :expenseId")
    fun getExpenseById(expenseId: Int): Expense?

    @Query("SELECT * FROM expenses")
    fun getAllExpenses(): LiveData<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses WHERE memberId = :memberId")
    fun getTotalSpentByMemberId(memberId: Int): LiveData<Double>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM expenses")
    fun getTotalSpent(): LiveData<Double>
}
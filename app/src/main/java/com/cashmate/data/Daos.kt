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

    @Query("""
    WITH RECURSIVE balance_table AS (
        SELECT m.id AS memberId, m.name, COALESCE(SUM(e.amount), 0) - 
        (SELECT COALESCE(SUM(e.amount), 0) / COUNT(DISTINCT m.id) FROM members m LEFT JOIN expenses e ON m.id = e.memberId) AS balance
        FROM members m
        LEFT JOIN expenses e ON m.id = e.memberId
        GROUP BY m.id
    ),
    sorted_balance AS (
        SELECT memberId, name, balance FROM balance_table WHERE balance != 0 ORDER BY balance DESC
    )
    SELECT payer.name AS payerName, receiver.name AS receiverName, MIN(payer.balance, -receiver.balance) AS amount
    FROM sorted_balance AS payer, sorted_balance AS receiver
    WHERE payer.balance > 0 AND receiver.balance < 0
""")
    fun calculateMinimalTransactions(): LiveData<List<Transaction>>


}

@Dao
interface ExpenseDao {
    @Insert
    fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE memberId = :memberId")
    fun getExpensesByMemberId(memberId: Int): List<Expense>

    @Query("SELECT * FROM expenses WHERE id = :expenseId")
    fun getExpenseById(expenseId: Int): Expense?

    @Query("SELECT * FROM expenses ORDER BY id DESC")
    fun getAllExpenses(): LiveData<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses WHERE memberId = :memberId")
    fun getTotalSpentByMemberId(memberId: Int): LiveData<Double>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM expenses")
    fun getTotalSpent(): LiveData<Double>
}
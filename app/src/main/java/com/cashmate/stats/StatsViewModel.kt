package com.cashmate.viewmodel

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.cashmate.data.AppDatabase
import com.cashmate.data.ExpenseDao
import com.cashmate.data.MemberDao
import com.cashmate.data.MemberWithExpense
import com.cashmate.data.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    ) : ViewModel() {
    private val cashMateDatabase = AppDatabase.getDatabase(context)

    val membersWithExpenses = cashMateDatabase.memberDao().getMembersWithTotalSpent().asFlow()
    val totalExpense = cashMateDatabase.expenseDao().getTotalSpent().asFlow()


    val transactions = cashMateDatabase.memberDao().calculateMinimalTransactions().asFlow()

//TODO fix minimal transactions logic

//        val transactions = liveData(Dispatchers.Default) {
//        val membersWithExpenses = cashMateDatabase.memberDao().getMembersWithTotalSpent().asFlow().asLiveData().value ?: return@liveData
//        val transactions = calculateMinimalTransactions(membersWithExpenses)
//        if (transactions.isEmpty()) {
//            println("No transactions to display")
//        } else {
//            println("Transactions emitted: $transactions")
//        }
//        emit(transactions)
//    }
//    val transactionsFlow = transactions.asFlow()

//    private fun calculateMinimalTransactions(membersWithExpenses: List<MemberWithExpense>): List<Transaction> {
//        val totalSpent = membersWithExpenses.sumOf { it.totalSpent }
//        val averageSpent = totalSpent / membersWithExpenses.size
//
//        val balances = membersWithExpenses.map {
//            it.id to (it.totalSpent - averageSpent)
//        }.toMap()
//
//        val creditors = balances.filter { it.value > 0 }.toList().toMutableList()
//        val debtors = balances.filter { it.value < 0 }.toList().toMutableList()
//
//        val transactions = mutableListOf<Transaction>()
//
//        while (creditors.isNotEmpty() && debtors.isNotEmpty()) {
//            val creditor = creditors.first()
//            val debtor = debtors.first()
//
//            val amount = minOf(creditor.second, -debtor.second)
//
//            transactions.add(
//                Transaction(
//                    payerName = membersWithExpenses.first { it.id == debtor.first }.name,
//                    receiverName = membersWithExpenses.first { it.id == creditor.first }.name,
//                    amount = amount
//                )
//            )
//
//            val updatedCreditorBalance = creditor.second - amount
//            val updatedDebtorBalance = debtor.second + amount
//
//            if (updatedCreditorBalance == 0.0) creditors.removeAt(0) else creditors[0] = creditor.first to updatedCreditorBalance
//            if (updatedDebtorBalance == 0.0) debtors.removeAt(0) else debtors[0] = debtor.first to updatedDebtorBalance
//        }
//
//        return transactions
//    }

}


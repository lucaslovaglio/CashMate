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

//    val membersWithExpenses = cashMateDatabase.memberDao().getMembersWithTotalSpent()
val membersWithExpenses = liveData(Dispatchers.IO) {
        val members = cashMateDatabase.memberDao().getMembersWithTotalSpent()
        emit(members)
    }.asFlow()


    val totalExpense = cashMateDatabase.expenseDao().getTotalSpent().asFlow()


//    val transactions = cashMateDatabase.memberDao().calculateMinimalTransactions().asFlow()

    val transactions = liveData(Dispatchers.IO) {
        val members = cashMateDatabase.memberDao().getMembersWithTotalSpent()
        emit(calculateMinimalTransactions(members))
    }.asFlow()


    private fun calculateMinimalTransactions(members: List<MemberWithExpense>): List<Transaction> {
        val totalExpense = members.sumOf { it.totalSpent }
        val averageExpense = totalExpense / members.size

        // Calcula los balances
        val balances = members.map {
            it.id to it.totalSpent - averageExpense
        }.toMutableList()

        // Separa los acreedores y deudores
        val creditors = balances.filter { it.second > 0 }.sortedByDescending { it.second }.toMutableList()
        val debtors = balances.filter { it.second < 0 }.sortedBy { it.second }.toMutableList()

        val transactions = mutableListOf<Transaction>()

        while (debtors.isNotEmpty() && creditors.isNotEmpty()) {
            val debtor = debtors[0]
            val creditor = creditors[0]

            // Determina el monto a pagar
            val amountToPay = minOf(-debtor.second, creditor.second)

            // Crea la transacción
            transactions.add(
                Transaction(
                    fromMemberId = debtor.first,
                    toMemberId = creditor.first,
                    amount = amountToPay,
                    payerName = members.find { it.id == debtor.first }?.name ?: "Unknown",
                    receiverName = members.find { it.id == creditor.first }?.name ?: "Unknown"
                )
            )

            // Actualiza los balances
            balances[balances.indexOf(debtor)] = debtor.first to (debtor.second + amountToPay)
            balances[balances.indexOf(creditor)] = creditor.first to (creditor.second - amountToPay)

            // Si el deudor ya pagó toda su deuda, lo elimina
            if (balances.find { it.first == debtor.first }?.second == 0.0) {
                debtors.removeAt(0)
            }

            // Si el acreedor ha recibido todo el pago, lo elimina
            if (balances.find { it.first == creditor.first }?.second == 0.0) {
                creditors.removeAt(0)
            }

            // Aquí actualizamos los balances finales
            debtors.forEach { debtor ->
                val updatedBalance = balances.find { balance -> balance.first == debtor.first }
                if (updatedBalance != null) {
                    debtors[debtors.indexOfFirst { it.first == debtor.first }] = debtor.first to updatedBalance.second
                }
            }

            creditors.forEach { creditor ->
                val updatedBalance = balances.find { balance -> balance.first == creditor.first }
                if (updatedBalance != null) {
                    creditors[creditors.indexOfFirst { it.first == creditor.first }] = creditor.first to updatedBalance.second
                }
            }
        }

        return transactions
    }




}


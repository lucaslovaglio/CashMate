package com.cashmate.ui.stats

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cashmate.data.MemberWithExpense
import com.cashmate.data.Transaction
import com.cashmate.home.HomeViewModel
import com.cashmate.viewmodel.StatsViewModel

@Composable
fun Stats() {
    val viewModel = hiltViewModel<StatsViewModel>()
    val membersWithExpenses by viewModel.membersWithExpenses.collectAsState(initial = emptyList())
    val totalExpense by viewModel.totalExpense.collectAsState(initial = 0.0)
    val transactions by viewModel.transactions.collectAsState(emptyList())

    StatsComposable(
        membersWithExpenses = membersWithExpenses,
        totalSpent = totalExpense,
        transactions = transactions
    )

}

@Composable
fun StatsComposable(
    membersWithExpenses: List<MemberWithExpense>,
    totalSpent: Double,
    transactions: List<Transaction>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Estadísticas del Viaje",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Gasto Total: $${String.format("%.2f", totalSpent)}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (transactions.isEmpty()) {
            Text(text = "No transactions available")
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(transactions) { transaction ->
                    TransactionItem(transaction)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Gastos por Miembro:",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn {
            items(membersWithExpenses) { member ->
                com.cashmate.ui.home.MemberExpenseItem(member, totalSpent, membersWithExpenses.size)
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "${transaction.payerName} debe a ${transaction.receiverName}")
            Text(text = "$${String.format("%.2f", transaction.amount)}")
        }
    }
}

@Composable
fun MemberExpenseItem(member: MemberWithExpense) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = member.name)
            Text(text = "Gastó: $${String.format("%.2f", member.totalSpent)}")
        }
    }
}

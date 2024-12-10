package com.cashmate.ui.stats

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cashmate.R
import com.cashmate.data.MemberWithExpense
import com.cashmate.data.Transaction
import com.cashmate.home.HomeViewModel
import com.cashmate.viewmodel.StatsViewModel

@Composable
fun Stats() {
    val viewModel = hiltViewModel<StatsViewModel>()
    val membersWithExpenses by viewModel.membersWithExpenses.collectAsState(initial = emptyList())
    val totalExpense by viewModel.totalExpense.collectAsState(initial = 0.0)
    val transactions by viewModel.transactions.collectAsState(initial = emptyList())


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
            text = stringResource(R.string.trip_stats),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = stringResource(R.string.total_spent) + ": $${String.format("%.2f", totalSpent)}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.debt_summary_title) + ":",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (transactions.isEmpty()) {
            Text(text = stringResource(R.string.empty_transactions))
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(transactions) { transaction ->
                    TransactionItem(transaction)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.expenses_by_member) + ":",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn {
            items(membersWithExpenses) { member ->
                MemberExpenseItem(member, totalSpent, membersWithExpenses.size)
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
fun MemberExpenseItem(member: MemberWithExpense, totalExpense: Double, membersQty: Int) {
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
            Text(text = stringResource(R.string.spent) + ": $${String.format("%.2f", member.totalSpent)}")
            Text(text = if (member.totalSpent <= totalExpense / membersQty) {
                "${stringResource(R.string.owes)}: $${String.format("%.2f", (totalExpense / membersQty) - member.totalSpent)}"
            } else {
                "${stringResource(R.string.is_owed)}: $${String.format("%.2f", ((totalExpense / membersQty) - member.totalSpent) * -1)}"
            })
        }
    }
}

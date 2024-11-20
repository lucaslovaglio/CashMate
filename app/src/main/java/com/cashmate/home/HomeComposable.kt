package com.cashmate.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cashmate.home.HomeViewModel
import com.cashmate.data.Member
import androidx.compose.ui.res.stringResource
import com.cashmate.R
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import com.cashmate.common.AddExpenseButton
import com.cashmate.common.BottomSheetContent
import com.cashmate.data.MemberWithExpense

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val members by homeViewModel.members.collectAsState(initial = emptyList())
    val membersWithExpenses by homeViewModel.membersWithExpenses.collectAsState(initial = emptyList())
    val totalExpense by homeViewModel.totalExpense.collectAsState(initial = 0.0)

    var showBottomModal by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val tripName = "Trip Name"


    if (showBottomModal) {
        BottomSheetContent(
            members = members,
            onAddExpense = { member, amount, description, isDollar ->
                homeViewModel.insertExpense(member.id, amount, description, isDollar)
            },
            onAddMember = { name ->
                homeViewModel.insertMember(Member(0, name))
            },
            onDismissRequest = { showBottomModal = false }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Trip Name
                Text(
                    text = tripName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // Expense Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ExpenseCard(
                        title = stringResource(R.string.average_spending),
                        amount = if (members.isEmpty()) totalExpense else totalExpense / members.size
                    )
                    ExpenseCard(
                        title = stringResource(R.string.total_spent),
                        amount = totalExpense
                    )
                }

                // Member Title
                Text(
                    text = stringResource(R.string.members_title),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )

                // Member list with expenses
                LazyColumn {
                    items(membersWithExpenses) { member ->
                        MemberExpenseItem(member, totalExpense, members.size)
                    }
                }
            }



            // FloatingActionButton
            AddExpenseButton(
                onClick = { showBottomModal = true },
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}




@Composable
fun ExpenseCard(title: String, amount: Double) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(text = "$${String.format("%.2f", amount)}", style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun MemberExpenseItem(member: MemberWithExpense, totalExpense: Double, membersQty: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = member.name)
        Text(text = "${stringResource(R.string.spent)}: $${String.format("%.2f", member.totalSpent)}")
        Text(text = if (member.totalSpent <= totalExpense / membersQty) {
            "${stringResource(R.string.owes)}: $${String.format("%.2f", (totalExpense / membersQty) - member.totalSpent)}"
        } else {
            "${stringResource(R.string.is_owed)}: $${String.format("%.2f", ((totalExpense / membersQty) - member.totalSpent) * -1)}"
        })
    }
}

@Preview
@Composable
fun PreviewHome() {
    Home()
}

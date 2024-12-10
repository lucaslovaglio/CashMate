package com.cashmate.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.cashmate.logs.LogsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val logsViewModel = hiltViewModel<LogsViewModel>()
    val members by homeViewModel.members.collectAsState(initial = emptyList())
    val totalExpense by homeViewModel.totalExpense.collectAsState(initial = 0.0)

    var showBottomModal by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val tripName by homeViewModel.tripName.collectAsState(initial = "Default Trip")
    var isEditingTripName by remember { mutableStateOf(false) }
    var editedTripName by remember { mutableStateOf(tripName) }

    if (showBottomModal) {
        BottomSheetContent(
            members = members,
            onAddExpense = { member, amount, description, isDollar ->
                logsViewModel.insertExpense(member.id, amount, description, isDollar)
            },
            onAddMember = { name ->
                logsViewModel.insertMember(Member(0, name))
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (isEditingTripName) {
                        TextField(
                            value = editedTripName,
                            onValueChange = { editedTripName = it },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                        IconButton(onClick = {
                            homeViewModel.updateTripName(editedTripName)
                            isEditingTripName = false
                        }) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Save Name",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        Text(
                            text = tripName ?: "Nombre del viaje",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            editedTripName = tripName ?: ""
                            isEditingTripName = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Name",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

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
                    items(members) { member ->
                        MemberItem(
                            member,
                            onDeleteMember = { memberId ->
                                homeViewModel.deleteMemberWithExpenses(memberId)
                            }
                        )
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
            .width(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(text = "$${String.format("%.2f", amount)}", style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun MemberItem(member: Member, onDeleteMember: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = member.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = { onDeleteMember(member.id) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Member",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHome() {
    Home()
}

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
import androidx.compose.foundation.layout.* // Asegúrate de incluir esta importación
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.rememberCoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val members by homeViewModel.members.collectAsState(initial = emptyList())
    val totalExpense by homeViewModel.totalExpense.collectAsState(initial = 0.0)

    var showBottomModal by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val tripName = "Trip Name"


    // BottomSheet State
    if (showBottomModal) {
        ModalBottomSheet(
            onDismissRequest = { showBottomModal = false }
        ) {
            var selectedTabIndex by remember { mutableStateOf(0) }
            val tabTitles = listOf("Add Expense", "Add Member")

            // Tabs
            Column {
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title) }
                        )
                    }
                }

                // Content based on selected tab
                when (selectedTabIndex) {
                    0 -> AddExpenseContent(
                        members = members,
                        onAddExpense = { member, amount, description ->
                            homeViewModel.addExpenseToMember(member.id, amount)
                            showBottomModal = false
                        }
                    )
                    1 -> AddMemberContent(
                        onAddMember = { name ->
                            homeViewModel.insertMember(Member(0, name, 0.0, 0.0))
                            showBottomModal = false
                        }
                    )
                }
            }
        }
    }

    // Main UI
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
                // Título del viaje
                Text(
                    text = tripName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // Cards de gastos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ExpenseCard(
                        title = stringResource(R.string.average_spending),
                        amount = totalExpense / members.size
                    )
                    ExpenseCard(
                        title = stringResource(R.string.total_spent),
                        amount = totalExpense
                    )
                }

                // Título de miembros
                Text(
                    text = stringResource(R.string.members_title),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )

                // Lista de miembros con sus gastos
                LazyColumn {
                    items(members) { member ->
                        MemberExpenseItem(member, totalExpense, members.size)
                    }
                }
            }



            // FloatingActionButton
            FloatingActionButton(
                onClick = { showBottomModal = true },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun AddExpenseContent(
    members: List<Member>,
    onAddExpense: (Member, Double, String) -> Unit
) {
    var selectedMember by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Add New Expense", style = MaterialTheme.typography.headlineMedium)

        // Dropdown for member selection
        var expanded by remember { mutableStateOf(false) }
        Box {
            TextButton(onClick = { expanded = !expanded }) {
                Text(selectedMember.ifEmpty { "Select a Member" })
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                members.forEach { member ->
                    DropdownMenuItem(
                        text = { Text(text = member.name) },
                        onClick = {
                            selectedMember = member.name
                            expanded = false
                        }
                    )
                }
            }
        }

        // Amount input
        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // Description input
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    val member = members.find { it.name == selectedMember }
                    if (member != null && amount.isNotEmpty() && description.isNotEmpty()) {
                        onAddExpense(member, amount.toDouble(), description)
                    }
                }
            ) {
                Text("Add")
            }
        }
    }
}

@Composable
fun AddMemberContent(
    onAddMember: (String) -> Unit
) {
    var memberName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Add New Member", style = MaterialTheme.typography.headlineMedium)

        // Name input
        TextField(
            value = memberName,
            onValueChange = { memberName = it },
            label = { Text("Name") }
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    if (memberName.isNotEmpty()) {
                        onAddMember(memberName)
                    }
                }
            ) {
                Text("Add")
            }
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
fun MemberExpenseItem(member: Member, totalExpense: Double, membersQty: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = member.name)
        Text(text = "${stringResource(R.string.spent)}: $${String.format("%.2f", member.spent)}")
        Text(text = if (member.spent < totalExpense / membersQty) {
            "${stringResource(R.string.owes)}: $${String.format("%.2f", (totalExpense / membersQty) - member.spent)}"
        } else {
            "${stringResource(R.string.is_owed)}: $${String.format("%.2f", ((totalExpense / membersQty) - member.spent) * -1)}"
        })
    }
}

@Preview
@Composable
fun PreviewHome() {
    Home()
}

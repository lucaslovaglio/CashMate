package com.cashmate.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cashmate.data.Member
import androidx.compose.material3.*
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.cashmate.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    members: List<Member>,
    onAddExpense: (Member, Double, String, Boolean) -> Unit,
    onAddMember: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf(stringResource(R.string.add_expense), stringResource(R.string.add_member))

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        modifier = Modifier.fillMaxWidth()
    ) {
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

            when (selectedTabIndex) {
                0 -> AddExpenseContent(
                    members = members,
                    onAddExpense = { member, amount, description, isDollar ->
                        onAddExpense(member, amount, description, isDollar)
                        onDismissRequest()
                    }
                )
                1 -> AddMemberContent(
                    onAddMember = { name ->
                        onAddMember(name)
                        onDismissRequest()
                    }
                )
            }
        }
    }
}

@Composable
fun AddExpenseContent(
    members: List<Member>,
    onAddExpense: (Member, Double, String, Boolean) -> Unit
) {
    var selectedMember by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isDollar by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(stringResource(R.string.add_new_expense), style = MaterialTheme.typography.headlineMedium)

        // Dropdown for member selection
        var expanded by remember { mutableStateOf(false) }
        Box {
            TextButton(onClick = { expanded = !expanded }) {
                Text(selectedMember.ifEmpty { stringResource(R.string.select_member) })
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
            label = { Text(stringResource(R.string.amount)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Description input
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.description)) },
            modifier = Modifier.fillMaxWidth()
        )

        // Currency selection
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(stringResource(R.string.is_dollar))
            Checkbox(
                checked = isDollar,
                onCheckedChange = { isDollar = it }
            )
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    val member = members.find { it.name == selectedMember }
                    if (member != null && amount.isNotEmpty() && description.isNotEmpty()) {
                        onAddExpense(member, amount.toDouble(), description, isDollar)
                    }
                }
            ) {
                Text(stringResource(R.string.add))
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
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(stringResource(R.string.add_new_member), style = MaterialTheme.typography.headlineMedium)

        // Name input
        TextField(
            value = memberName,
            onValueChange = { memberName = it },
            label = { Text(stringResource(R.string.name)) },
            modifier = Modifier.fillMaxWidth()
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    if (memberName.isNotEmpty()) {
                        onAddMember(memberName)
                    }
                }
            ) {
                Text(stringResource(R.string.add))
            }
        }
    }
}

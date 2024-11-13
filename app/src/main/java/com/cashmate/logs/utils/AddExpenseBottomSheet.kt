import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.cashmate.home.Member

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseBottomSheet(members: List<Member>, onAddExpense: (String, Double, String) -> Unit) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var selectedMember by remember { mutableStateOf(members.first().name) }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = { showBottomSheet = true }) {
            Text("Add Expense")
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Add New Expense", style = MaterialTheme.typography.headlineMedium)

                    // Dropdown for member selection
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        TextButton(onClick = { expanded = !expanded }) {
                            Text(selectedMember)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            members.forEach { member ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = member.name)
                                    },
                                    onClick = {
                                        selectedMember = member.name
                                        expanded = false
                                    })
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

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                if (selectedMember.isNotEmpty() && amount.isNotEmpty() && description.isNotEmpty()) {
                                    onAddExpense(selectedMember, amount.toDouble(), description)
                                    showBottomSheet = false // Close the bottom sheet
                                }
                            },
                        ) {
                            Text("Add")
                        }
                    }
                }
            }
        }
    }
}

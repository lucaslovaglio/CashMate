import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cashmate.R
import com.cashmate.home.HomeViewModel
import com.cashmate.logs.LogsViewModel
import com.cashmate.logs.Movement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Logs() {
    val logsViewModel = hiltViewModel<LogsViewModel>()
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val members by homeViewModel.members.collectAsState()
//    val movements by logsViewModel.movements.collectAsState()
//
//    var showBottomModal by remember {
//        mutableStateOf(false)
//    }
//
//    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
//    var selectedMember by remember { mutableStateOf(members.first().name) }
//    var amount by remember { mutableStateOf("") }
//    var description by remember { mutableStateOf("") }
//
//    fun handleAddExpense(memberName: String, amount: Double, description: String) {
//        logsViewModel.addMovement(memberName, amount, "2024-09-19", description)
//        homeViewModel.addExpense(memberName, amount)
//    }

//    Column(
//        modifier = Modifier
//            .padding(16.dp)
//            .fillMaxWidth(),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        Text(
//            text = stringResource(R.string.logs_title),
//            style = MaterialTheme.typography.headlineMedium,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        LazyColumn(
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(movements.size) { index ->
//                MovementCard(movement = movements[index])
//            }
//        }
//
//        FloatingActionButton(
//            onClick = { showBottomModal = !showBottomModal },
//            modifier = Modifier
//                .padding(bottom = 10.dp)
//        ) {
//            Icon(
//                imageVector = Icons.Default.Add,
//                contentDescription = "Add Expense"
//            )
//        }
//        if(showBottomModal) {
//            ModalBottomSheet(onDismissRequest = { showBottomModal = !showBottomModal }) {
//                Column(
//                    modifier = Modifier.padding(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    Text("Add New Expense", style = MaterialTheme.typography.headlineMedium)
//
//                    // Dropdown for member selection
//                    var expanded by remember { mutableStateOf(false) }
//                    Box {
//                        TextButton(onClick = { expanded = !expanded }) {
//                            Text(selectedMember)
//                        }
//                        DropdownMenu(
//                            expanded = expanded,
//                            onDismissRequest = { expanded = false }
//                        ) {
//                            members.forEach { member ->
//                                DropdownMenuItem(
//                                    text = {
//                                        Text(text = member.name)
//                                    },
//                                    onClick = {
//                                        selectedMember = member.name
//                                        expanded = false
//                                    })
//                            }
//                        }
//                    }
//
//                    // Amount input
//                    TextField(
//                        value = amount,
//                        onValueChange = { amount = it },
//                        label = { Text("Amount") },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                    )
//
//                    // Description input
//                    TextField(
//                        value = description,
//                        onValueChange = { description = it },
//                        label = { Text("Description") }
//                    )
//
//                    Box(
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Button(
//                            onClick = {
//                                if (selectedMember.isNotEmpty() && amount.isNotEmpty() && description.isNotEmpty()) {
//                                    handleAddExpense(selectedMember, amount.toDouble(), description)
//                                    showBottomModal = false
//                                }
//                            },
//                        ) {
//                            Text("Add")
//                        }
//                    }
//                }
//            }
//        AddExpenseBottomSheet(members = members) { memberName, amount, description ->
//            logsViewModel.addMovement(memberName, amount, "2024-09-19", description)
//            homeViewModel.addExpense(memberName, amount)
//        }
//    }
}



//@Composable
//fun MovementCard(movement: Movement) {
//    Card(
//        shape = RoundedCornerShape(8.dp),
//        modifier = Modifier.fillMaxWidth(),
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth()
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(text = movement.memberName, style = MaterialTheme.typography.bodyMedium)
//                Text(text = movement.date, style = MaterialTheme.typography.bodySmall)
//            }
//            Text(
//                text = "${stringResource(R.string.amount)}: \$${movement.amount}",
//                style = MaterialTheme.typography.bodyLarge,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//            Text(
//                text = movement.description,
//                style = MaterialTheme.typography.bodySmall,
//                modifier = Modifier.padding(top = 4.dp)
//            )
//        }
//    }
//}

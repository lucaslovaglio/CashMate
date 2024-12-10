import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cashmate.logs.LogsViewModel
import com.cashmate.common.AddExpenseButton
import com.cashmate.common.BottomSheetContent
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cashmate.data.Member
import com.cashmate.R
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import com.cashmate.data.Expense
import com.cashmate.data.ExpenseWithMemberName


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Logs() {
    val logsViewModel = hiltViewModel<LogsViewModel>()
    val members by logsViewModel.members.collectAsState(initial = emptyList())

    var showBottomModal by remember { mutableStateOf(false) }

    val expenses by logsViewModel.expenses.collectAsState(initial = emptyList())

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.logs_title),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(expenses.size) { index ->
                        MovementCard(
                            expense = expenses[index],
                            onDeleteClick = { expenseId ->
                                logsViewModel.deleteExpense(expenseId)
                            }
                        )
                    }
                }

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
            }
            AddExpenseButton(
                onClick = { showBottomModal = true },
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}



@Composable
fun MovementCard(expense: ExpenseWithMemberName, onDeleteClick: (Int) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = expense.memberName, style = MaterialTheme.typography.bodyMedium)
                Text(text = expense.date.toString(), style = MaterialTheme.typography.bodySmall)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${stringResource(R.string.amount)}: \$${expense.amount}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = expense.description,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                IconButton(onClick = { onDeleteClick(expense.id) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                    )
                }
            }
        }
    }
}

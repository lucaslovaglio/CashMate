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
import com.cashmate.home.Member
import androidx.compose.ui.res.stringResource
import com.cashmate.R

@Composable
fun Home() {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val members by homeViewModel.members.collectAsState()
    val tripName by homeViewModel.tripName.collectAsState()
    val averageExpense by homeViewModel.averageExpense.collectAsState()
    val totalExpense by homeViewModel.totalExpense.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = tripName,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ExpenseCard(title = stringResource(R.string.average_spending), amount = averageExpense)
                ExpenseCard(title = stringResource(R.string.total_spent), amount = totalExpense)
            }

            Text(
                text = stringResource(R.string.members_title),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
            LazyColumn {
                items(members) { member ->
                    MemberExpenseItem(member)
                }
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

@Composable
fun MemberExpenseItem(member: Member) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = member.name)
        Text(text = "${stringResource(R.string.spent)}: $${String.format("%.2f", member.spent)}")
        Text(text = if (member.balance >= 0) {
            "${stringResource(R.string.owed)}: $${String.format("%.2f", member.balance)}"
        } else {
            "${stringResource(R.string.owe)}: $${String.format("%.2f", -member.balance)}"
        })
    }
}

@Preview
@Composable
fun PreviewHome() {
    Home()
}

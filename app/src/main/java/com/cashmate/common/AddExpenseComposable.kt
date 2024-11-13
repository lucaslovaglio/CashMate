import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.cashmate.navigation.BottomBar
import com.cashmate.navigation.CahsMateScreen

@Composable
fun AddExpenseModal(
    onDismiss: () -> Unit,
    onSave: (String, Double) -> Unit
) {
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var amount by remember { mutableStateOf(TextFieldValue("")) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Agregar Nuevo Gasto", style = MaterialTheme.typography.headlineSmall)
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Monto") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        val parsedAmount = amount.text.toDoubleOrNull() ?: 0.0
                        onSave(description.text, parsedAmount)
                        onDismiss()
                    }) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBarWithAddExpense(
    onNavigate: (String) -> Unit,
    onSaveExpense: (String, Double) -> Unit
) {
    var isModalVisible by rememberSaveable { mutableStateOf(false) }

    BottomBar(onNavigate = { screen ->
        if (screen == CahsMateScreen.Home.name) {
            isModalVisible = true
        } else {
            onNavigate(screen)
        }
    })

    if (isModalVisible) {
        AddExpenseModal(
            onDismiss = { isModalVisible = false },
            onSave = { description, amount ->
                onSaveExpense(description, amount)
            }
        )
    }
}

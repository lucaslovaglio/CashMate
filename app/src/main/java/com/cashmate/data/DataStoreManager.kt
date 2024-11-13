import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "CASHMATE_DATA_STORE")

object PreferencesKeys {
    val TRIP_NAME_KEY = stringPreferencesKey("trip_name")
    val TOTAL_EXPENSE_KEY = doublePreferencesKey("total_expense")
    val AVERAGE_EXPENSE_KEY = doublePreferencesKey("average_expense")
}

suspend fun <T> saveToDataStore(context: Context, value: T, key: Preferences.Key<T>) {
    context.dataStore.edit { preferences ->
        preferences[key] = value
    }
}

fun <T> getFromDataStore(context: Context, key: Preferences.Key<T>): Flow<T?> {
    return context.dataStore.data
        .map { preferences ->
            preferences[key]
        }
}

class DataStoreManager(private val context: Context) {

    suspend fun saveTripData(tripName: String, totalExpense: Double, averageExpense: Double) {
        saveToDataStore(context, tripName, PreferencesKeys.TRIP_NAME_KEY)
        saveToDataStore(context, totalExpense, PreferencesKeys.TOTAL_EXPENSE_KEY)
        saveToDataStore(context, averageExpense, PreferencesKeys.AVERAGE_EXPENSE_KEY)
    }

    val tripNameFlow: Flow<String?> = getFromDataStore(context, PreferencesKeys.TRIP_NAME_KEY)
    val totalExpenseFlow: Flow<Double?> = getFromDataStore(context, PreferencesKeys.TOTAL_EXPENSE_KEY)
    val averageExpenseFlow: Flow<Double?> = getFromDataStore(context, PreferencesKeys.AVERAGE_EXPENSE_KEY)
}

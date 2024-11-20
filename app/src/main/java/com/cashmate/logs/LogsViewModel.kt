package com.cashmate.logs

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.cashmate.data.AppDatabase
import com.cashmate.data.Expense
import com.cashmate.data.Member
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import androidx.compose.runtime.State
import com.cashmate.apiManager.ApiServiceImpl
import kotlinx.coroutines.flow.StateFlow


@HiltViewModel
class LogsViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {
    private val cashMateDatabase = AppDatabase.getDatabase(context)
    val members = cashMateDatabase.memberDao().getAllMembers().asFlow()
    val totalExpense = cashMateDatabase.expenseDao().getTotalSpent().asFlow()
    val membersWithExpenses = cashMateDatabase.memberDao().getMembersWithTotalSpent().asFlow()
    val expenses = cashMateDatabase.expenseDao().getAllExpenses().asFlow()

    private val apiService = ApiServiceImpl()

    private val _dollarExchangeRate = MutableStateFlow<Double?>(null)
    val dollarExchangeRate: StateFlow<Double?> = _dollarExchangeRate

    init {
//        fetchDollarExchangeRate()
    }

    private fun fetchDollarExchangeRate() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val exchangeRate = apiService.getExchangeRates().blue.value_avg
                _dollarExchangeRate.emit(exchangeRate)
            } catch (e: Exception) {
                println("Error fetching dollar exchange rate: ${e.message}")
            }
        }
    }


    fun insertExpense(memberId: Int, amount: Double, description: String, isDollar: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val finalAmount = if (isDollar) {
                val rate = _dollarExchangeRate.value ?: 2.0
                amount * rate
            } else {
                amount
            }
            val expense = Expense(0, memberId, finalAmount, description)
            cashMateDatabase.expenseDao().insertExpense(expense)
        }
    }

    fun insertMember(member: Member) {
        viewModelScope.launch(Dispatchers.IO) {
            cashMateDatabase.memberDao().insertMember(member)
        }
    }

    fun getMemberName(memberId: Int): State<String> {
        val memberName = mutableStateOf("")

        viewModelScope.launch {
            try {
                val member = cashMateDatabase.memberDao().getMemberById(memberId)
                memberName.value = member?.name ?: "Unknown" // Nombre por defecto
            } catch (e: Exception) {
                println("Error: $e")
                memberName.value = "Error"
            }
        }

        return memberName
    }


}

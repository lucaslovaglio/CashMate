package com.cashmate.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.cashmate.apiManager.ApiServiceImpl
import com.cashmate.data.AppDatabase
import com.cashmate.data.Expense
import com.cashmate.data.Member
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {
    private val cashMateDatabase = AppDatabase.getDatabase(context)
    private val apiService = ApiServiceImpl()
    val members = cashMateDatabase.memberDao().getAllMembers().asFlow()
    val totalExpense = cashMateDatabase.expenseDao().getTotalSpent().asFlow()
    val membersWithExpenses = cashMateDatabase.memberDao().getMembersWithTotalSpent().asFlow()

    private val _dollarExchangeRate = MutableStateFlow<Double?>(null)
    val dollarExchangeRate: StateFlow<Double?> = _dollarExchangeRate

    init {
        fetchDollarExchangeRate()
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
                val rate = _dollarExchangeRate.value ?: 1.0
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

}

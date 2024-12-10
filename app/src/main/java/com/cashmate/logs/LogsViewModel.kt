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
import androidx.lifecycle.liveData
import com.cashmate.apiManager.ApiServiceImpl
import kotlinx.coroutines.flow.StateFlow


@HiltViewModel
class LogsViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {
    private val cashMateDatabase = AppDatabase.getDatabase(context)
    val members = cashMateDatabase.memberDao().getAllMembers().asFlow()
    val totalExpense = cashMateDatabase.expenseDao().getTotalSpent().asFlow()
//    val membersWithExpenses = cashMateDatabase.memberDao().getMembersWithTotalSpent().asFlow()
    val membersWithExpenses = liveData(Dispatchers.IO) {
        val members = cashMateDatabase.memberDao().getMembersWithTotalSpent()
        emit(members)
    }.asFlow()

    val expenses = cashMateDatabase.expenseDao().getAllExpensesWithMemberName().asFlow()

    private val apiService = ApiServiceImpl()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _dollarExchangeRate = MutableStateFlow<Double?>(null)
    val dollarExchangeRate: StateFlow<Double?> = _dollarExchangeRate

    private val _needRetry = MutableStateFlow(false)
    val needRetry = _needRetry.asStateFlow()

    init {
        fetchDollarExchangeRate()
    }

    private fun fetchDollarExchangeRate() {
        _loading.value = true
        apiService.getExchangeRates(
            context,
            onSuccess = { response ->
                _dollarExchangeRate.value = response.blue.value_avg
                _needRetry.value = false
            },
            onFail = {
                _needRetry.value = true
            },
            loadingFinished = {
                _loading.value = false
            }
        )
    }


    fun insertExpense(memberId: Int, amount: Double, description: String, isDollar: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_needRetry.value) {
                fetchDollarExchangeRate()
            }
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

    fun deleteExpense(expenseId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            cashMateDatabase.expenseDao().deleteExpenseById(expenseId)
        }
    }

    fun insertMember(member: Member) {
        viewModelScope.launch(Dispatchers.IO) {
            cashMateDatabase.memberDao().insertMember(member)
        }
    }
}

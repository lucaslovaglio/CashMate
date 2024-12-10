package com.cashmate.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.cashmate.apiManager.ApiServiceImpl
import com.cashmate.data.AppDatabase
import com.cashmate.data.Expense
import com.cashmate.data.Member
import com.cashmate.data.Trip
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {
    private val cashMateDatabase = AppDatabase.getDatabase(context)
    val members = cashMateDatabase.memberDao().getAllMembers().asFlow()
    val totalExpense = cashMateDatabase.expenseDao().getTotalSpent().asFlow()
    val tripName = cashMateDatabase.tripDao().getTripName().asFlow()

    fun updateTripName(newName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cashMateDatabase.tripDao().insertOrUpdateTrip(Trip(id= 1, name = newName))
        }
    }

    fun deleteMemberWithExpenses(memberId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            cashMateDatabase.memberDao().deleteMemberAndExpenses(memberId)
            cashMateDatabase.expenseDao().deleteExpensesByMemberId(memberId)
        }
    }
}

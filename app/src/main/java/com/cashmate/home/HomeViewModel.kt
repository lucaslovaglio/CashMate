package com.cashmate.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.cashmate.data.AppDatabase
import com.cashmate.data.Member
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {
    private val cashMateDatabase = AppDatabase.getDatabase(context)

    val members = cashMateDatabase.memberDao().getAllMembers().asFlow()
    private val _totalExpense = MutableStateFlow(0.0)
    val totalExpense: StateFlow<Double> = _totalExpense

    init {
        fetchTotalExpense()
    }


    private fun fetchTotalExpense() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                _totalExpense.value = cashMateDatabase.memberDao().getTotalBalance()
            }
        }
    }

    fun addExpenseToMember(memberId: Int, amount: Double) {
        viewModelScope.launch {
            cashMateDatabase.memberDao().addExpenseToMember(memberId, amount)
            fetchTotalExpense()
        }
    }

    fun insertMember(member: Member) {
        viewModelScope.launch {
            cashMateDatabase.memberDao().insertMember(member)
        }
    }
}

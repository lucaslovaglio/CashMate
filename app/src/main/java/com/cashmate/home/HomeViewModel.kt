package com.cashmate.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.cashmate.data.AppDatabase
import com.cashmate.data.Expense
import com.cashmate.data.Member
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {
    private val cashMateDatabase = AppDatabase.getDatabase(context)
    val members = cashMateDatabase.memberDao().getAllMembers().asFlow()
    val totalExpense = cashMateDatabase.expenseDao().getTotalSpent().asFlow()
    val membersWithExpenses = cashMateDatabase.memberDao().getMembersWithTotalSpent().asFlow()



    fun insertExpense(memberId: Int, amount: Double, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val expense = Expense(0, memberId, amount, description)
            cashMateDatabase.expenseDao().insertExpense(expense)
        }
    }

    fun insertMember(member: Member) {
        viewModelScope.launch(Dispatchers.IO) {
            cashMateDatabase.memberDao().insertMember(member)
        }
    }

}

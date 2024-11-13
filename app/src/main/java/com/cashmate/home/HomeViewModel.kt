package com.cashmate.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashmate.logs.Movement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private var _members = MutableStateFlow(listOf<Member>())
    val members = _members.asStateFlow()

    private val _tripName = MutableStateFlow("Nombre del Viaje")
    val tripName = _tripName.asStateFlow()

    private val _averageExpense = MutableStateFlow(0.0)
    val averageExpense: StateFlow<Double> get() = _averageExpense

    private val _totalExpense = MutableStateFlow(0.0)
    val totalExpense: StateFlow<Double> get() = _totalExpense

    fun addExpense(memberName: String, amount: Double) {
        viewModelScope.launch {
            updateMembers(memberName, amount)
        }
    }

    private fun updateMembers(memberName: String, amount: Double) {
        val updatedMembers = _members.value.map { member ->
            if (member.name == memberName) {
                member.copy(spent = member.spent + amount)
            } else {
                member
            }
        }

        _members.value = updatedMembers
        calculateExpenses(updatedMembers)
    }

    private fun calculateExpenses(updatedMembers: List<Member>) {
        val total = updatedMembers.sumOf { it.spent }
        val average = if (updatedMembers.isNotEmpty()) total / updatedMembers.size else 0.0
        _totalExpense.value = total
        _averageExpense.value = average
    }
}

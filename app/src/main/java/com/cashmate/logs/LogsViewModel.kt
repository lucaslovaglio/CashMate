package com.cashmate.logs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogsViewModel @Inject constructor() : ViewModel() {
    private var _movements = MutableStateFlow(listOf<Movement>())
    val movements = _movements.asStateFlow()

    fun addMovement(memberName: String, amount: Double, date: String, description: String) {
        val movement = Movement(memberName, amount, date, description)
        val newList = _movements.value + movement
        viewModelScope.launch {
            _movements.emit(newList)
        }
    }
}

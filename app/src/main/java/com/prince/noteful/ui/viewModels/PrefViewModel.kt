package com.prince.noteful.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prince.noteful.repo.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrefViewModel @Inject constructor(
    private val prefs: PreferencesRepository
): ViewModel() {

    val userName = prefs.userName.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
    fun saveUserName(name: String) = viewModelScope.launch {
        prefs.saveUserName(name.trim())
    }

    val isGridView: StateFlow<Boolean> = prefs.isGridView.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    fun setGridView() = viewModelScope.launch {
        prefs.setGridView(!isGridView.value)
    }
}
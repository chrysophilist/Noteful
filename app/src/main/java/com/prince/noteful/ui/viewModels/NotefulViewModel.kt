package com.prince.noteful.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prince.noteful.data.local.NoteEntity
import com.prince.noteful.data.repo.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotefulViewModel @Inject constructor(
    private val repo: NotesRepository
): ViewModel() {

    val notes = repo.notes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    private val _activeNote = MutableStateFlow<NoteEntity?>(null)
    val activeNote: StateFlow<NoteEntity?> = _activeNote

    fun loadNote(id:String) = viewModelScope.launch {
        _activeNote.value = repo.getNoteById(id)
    }

    fun saveNote(note: NoteEntity) = viewModelScope.launch {
        repo.upsertNote(note)

        _activeNote.value = note
    }

    fun deleteNote(note: NoteEntity) = viewModelScope.launch {
        repo.deleteNote(note)
    }

    suspend fun searchNote(query:String) = repo.searchNote(query)
}
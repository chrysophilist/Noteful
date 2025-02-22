package com.prince.noteful

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyViewModel : ViewModel() {
    private val _state = MutableStateFlow(ScreenState())
    val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
//                notesList = arrayListOf("Note 1", "Note 2", "Note 3")
                notesList = arrayListOf<String>()
            )
        }
    }

    fun updateNote(note: String) {
        _state.update {
            it.copy(
                note = note
            )
        }
    }

    fun addNote() {
        _state.update {
            it.copy(
                notesList = ArrayList(it.notesList).apply {
                    add(it.note)
                }
            )
        }
    }

    fun removeNote() {
        _state.update {
            it.copy(
                notesList = ArrayList(it.notesList).apply {
                    remove(it.note)
                }
            )
        }
    }
}

data class ScreenState(
    var notesList: ArrayList<String> = arrayListOf(),
    var note: String = ""
)
package com.prince.noteful.data.repo

import com.prince.noteful.data.local.NoteDao
import com.prince.noteful.data.local.NoteEntity
import kotlinx.coroutines.flow.Flow

class NotesRepository(
    private val dao: NoteDao
){
    val notes: Flow<List<NoteEntity>> = dao.getAllNotes()

    suspend fun getNoteById(id: String) = dao.getNoteById(id)

    suspend fun upsertNote(note: NoteEntity) = dao.upsertNote(note)

    suspend fun deleteNote(note: NoteEntity) = dao.deleteNote(note)

    suspend fun searchNote(query: String) = dao.searchNote(query)
}
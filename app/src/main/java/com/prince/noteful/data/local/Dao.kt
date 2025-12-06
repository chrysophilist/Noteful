package com.prince.noteful.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id:String): NoteEntity?

    @Upsert
    suspend fun upsertNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("""
        SELECT * FROM notes
        WHERE (title LIKE '%' || :query || '%' 
        OR content LIKE '%' || :query || '%')
        """)
    suspend fun searchNote(query:String): List<NoteEntity>
}
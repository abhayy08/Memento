package com.abhay.features.note.note_data.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.abhay.features.note.note_domain.model.Note
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id:Int): Note?

    @Upsert
    suspend fun insertNote(note:Note)

    @Delete
    suspend fun deleteNote(note:Note)
}
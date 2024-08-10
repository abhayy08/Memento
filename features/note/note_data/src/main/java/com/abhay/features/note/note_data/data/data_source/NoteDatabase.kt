package com.abhay.features.note.note_data.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.abhay.features.note.note_domain.model.Note

@Database(
    entities = [
        Note::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase: RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object{
        const val NOTEDATABASENAME = "notedb"
    }
}
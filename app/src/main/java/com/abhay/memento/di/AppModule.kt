package com.abhay.memento.di

import android.app.Application
import androidx.room.Room
import com.abhay.features.note.note_data.data.data_source.NoteDao
import com.abhay.features.note.note_data.data.data_source.NoteDatabase
import com.abhay.features.note.note_data.data.repository.NoteRepositoryImpl
import com.abhay.features.note.note_domain.repository.NoteRepository
import com.abhay.features.note.note_domain.use_cases.AddNote
import com.abhay.features.note.note_domain.use_cases.DeleteNote
import com.abhay.features.note.note_domain.use_cases.GetNote
import com.abhay.features.note.note_domain.use_cases.GetNotes
import com.abhay.features.note.note_domain.use_cases.NotesUseCases
import com.abhay.task_data.TodoDao
import com.abhay.task_data.TodoDatabase
import com.abhay.task_data.TodoRepository
import com.abhay.task_data.TodoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app, NoteDatabase::class.java, NoteDatabase.NOTEDATABASENAME
        ).build()
    }

    @Provides
    fun provideNotesDao(db: NoteDatabase): NoteDao {
        return db.noteDao
    }

    @Provides
    @Singleton
    fun provideNoteRepository(dao: NoteDao): NoteRepository {
        return NoteRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideNotesUseCases(repo: NoteRepositoryImpl): NotesUseCases {
        return NotesUseCases(
            getNotes = GetNotes(repo),
            getNote = GetNote(repo),
            addNote = AddNote(repo),
            deleteNote = DeleteNote(repo)
        )
    }

    @Provides
    @Singleton
    fun providesTaskDatabase(app: Application): TodoDatabase {
        return Room.databaseBuilder(
            app, TodoDatabase::class.java, "taskdb"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(db: TodoDatabase) : TodoRepository {
        return TodoRepositoryImpl(db.dao)
    }

}
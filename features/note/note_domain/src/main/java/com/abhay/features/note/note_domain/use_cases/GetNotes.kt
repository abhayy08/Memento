package com.abhay.features.note.note_domain.use_cases

import com.abhay.features.note.note_domain.model.Note
import com.abhay.features.note.note_domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotes(
    private val repository: NoteRepository
) {
    operator fun invoke(
    ): Flow<List<Note>> {
        return repository.getNotes()
    }
}
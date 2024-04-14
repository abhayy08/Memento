package com.abhay.features.note.note_domain.use_cases

import com.abhay.features.note.note_domain.model.Note
import com.abhay.features.note.note_domain.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note : Note) {
        repository.deleteNote(note)
    }
}
package com.abhay.features.note.note_ui.notes

import com.abhay.features.note.note_domain.model.Note

data class NotesState(
    val notes: List<Note> = emptyList()
)
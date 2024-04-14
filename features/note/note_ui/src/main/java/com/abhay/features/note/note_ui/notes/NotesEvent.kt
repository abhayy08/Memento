package com.abhay.features.note.note_ui.notes

import com.abhay.features.note.note_domain.model.Note

sealed class NotesEvent {
    data class AddNote(val note: Note) : NotesEvent()
    data class DeleteNote(val note: Note) : NotesEvent()
    object RestoreNote : NotesEvent()
}
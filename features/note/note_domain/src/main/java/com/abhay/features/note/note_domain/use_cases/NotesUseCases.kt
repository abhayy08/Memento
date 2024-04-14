package com.abhay.features.note.note_domain.use_cases

data class NotesUseCases(
    val getNotes: GetNotes,
    val deleteNote: DeleteNote,
    val addNote: AddNote,
    val getNote: GetNote
)
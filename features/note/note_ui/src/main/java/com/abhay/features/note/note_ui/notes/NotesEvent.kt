package com.abhay.features.note.note_ui.notes

import androidx.compose.ui.focus.FocusState

sealed class NotesEvent {
    object SaveNote : NotesEvent()
    object DeleteNote : NotesEvent()

    data class OpenOrCreateNote(val noteId: Int?) : NotesEvent()

    data class OnTitleChange(val title: String) : NotesEvent()
    data class OnContentChange(val content: String) : NotesEvent()

    object OnBackClick: NotesEvent()
    data class ChangeContentFocus(val focusState: FocusState): NotesEvent()
    data class ChangeTitleFocus(val focusState: FocusState): NotesEvent()
}
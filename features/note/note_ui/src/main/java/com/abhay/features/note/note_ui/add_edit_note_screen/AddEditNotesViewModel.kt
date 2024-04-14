package com.abhay.features.note.note_ui.add_edit_note_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhay.features.note.note_domain.model.InvalidNoteException
import com.abhay.features.note.note_domain.model.Note
import com.abhay.features.note.note_domain.use_cases.NotesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNotesViewModel @Inject constructor(
    private val notesUseCases: NotesUseCases, savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _titleState = mutableStateOf(NoteTextFieldState(hint = "Title"))
    val titleState: State<NoteTextFieldState> = _titleState

    private val _contentState = mutableStateOf(NoteTextFieldState(hint = "Note"))
    val contentState: State<NoteTextFieldState> = _contentState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var isNote = false

    private var currentNoteId: Int? = null
    lateinit var currentnote: Note

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    notesUseCases.getNote(noteId)?.also { note ->
                        currentnote = note
                        isNote = true
                        currentNoteId = note.id
                        _titleState.value = _titleState.value.copy(
                            text = note.title, isHintVisible = note.title == ""
                        )
                        _contentState.value = _contentState.value.copy(
                            text = note.content, isHintVisible = false
                        )
                    }
                }
            } else {
                isNote = false
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.ChangeContentFocus -> {
                _contentState.value = _contentState.value.copy(
                    isHintVisible = !event.focusState.isFocused && contentState.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                _titleState.value = _titleState.value.copy(
                    isHintVisible = !event.focusState.isFocused && titleState.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        if (!(titleState.value.text.isBlank() && contentState.value.text.isBlank())) {
                            notesUseCases.addNote(
                                note = Note(
                                    title = titleState.value.text,
                                    content = contentState.value.text,
                                    timeStamp = System.currentTimeMillis(),
                                    id = currentNoteId
                                )
                            )
                        }
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (_: InvalidNoteException) {

                    }
                }
            }

            is AddEditNoteEvent.onNoteChange -> {
                _contentState.value = _contentState.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.onTitleChange -> {
                _titleState.value = _titleState.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    notesUseCases.deleteNote(currentnote)
                    _eventFlow.emit(UiEvent.DeleteNote)
                }
            }
        }
    }

    sealed class UiEvent {
        data object SaveNote : UiEvent()
        data object DeleteNote : UiEvent()
    }
}
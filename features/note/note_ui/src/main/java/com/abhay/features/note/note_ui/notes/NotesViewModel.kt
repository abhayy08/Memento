package com.abhay.features.note.note_ui.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhay.features.note.note_domain.model.Note
import com.abhay.features.note.note_domain.use_cases.NotesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesUseCases: NotesUseCases
) : ViewModel() {
    private val _state = mutableStateOf(NotesState())
    val notesState: State<NotesState> = _state
    private var getNotesJob: Job? = null

    private val _clickedNoteId = mutableStateOf<Int?>(null)
    val noteId: Int? = _clickedNoteId.value

    private val _noteTitle = mutableStateOf<String>("")
    val noteTitle: State<String> = _noteTitle

    private val _isTitleHintVisible = mutableStateOf<Boolean>(true)
    val isTitleHintVisible: State<Boolean> = _isTitleHintVisible

    private val _noteContent = mutableStateOf<String>("")
    val noteContent: State<String> = _noteContent

    private val _isContentHintVisible = mutableStateOf<Boolean>(true)
    val isContentHintVisible: State<Boolean> = _isContentHintVisible

    private var clickedNoteTimeStamp: Long = 1

    var isNote = false

//    private var clickedNote: Note? = null

    init {
        getNotes()
    }

    fun onEvent(event: NotesEvent) {
        when (event) {

            is NotesEvent.DeleteNote -> { // call on delete note
                viewModelScope.launch {
                    notesUseCases.deleteNote(
                        note = Note(
                            title = _noteTitle.value,
                            content = _noteContent.value,
                            timeStamp = clickedNoteTimeStamp,
                            id = _clickedNoteId.value
                        )
                    )

                }
            }

            is NotesEvent.SaveNote -> { // call on save and then do back
                if (!(_noteTitle.value.isBlank() && _noteContent.value.isBlank())){
                    viewModelScope.launch {
                        notesUseCases.addNote(
                            note = Note(
                                title = _noteTitle.value,
                                content = _noteContent.value,
                                timeStamp = clickedNoteTimeStamp,
                                id = _clickedNoteId.value
                            )
                        )
//                    notesUseCases.addNote(note)
                    }
                }
                _noteTitle.value = ""
                _noteContent.value = ""
                clickedNoteTimeStamp = 1
                _clickedNoteId.value = null
                isNote = false
                _isContentHintVisible.value = true
                _isTitleHintVisible.value = true
            }

            is NotesEvent.OpenOrCreateNote -> { // call when tapped on fab (with null) or clicked on the note (with noteId)
                if (event.noteId != null) {
                    viewModelScope.launch {
                        val note = notesUseCases.getNote(event.noteId)
                        isNote = true
                        _clickedNoteId.value = event.noteId
                        _noteTitle.value = note!!.title
                        _noteContent.value = note.content
                        clickedNoteTimeStamp = note.timeStamp
                        _isContentHintVisible.value = false
                        _isTitleHintVisible.value = false
                    }
                } else {
                    _noteContent.value = ""
                    _noteTitle.value = ""
                    clickedNoteTimeStamp = System.currentTimeMillis()
                    _clickedNoteId.value = null
                    isNote = false
                    _isContentHintVisible.value = true
                    _isTitleHintVisible.value = true
                }
            }

            is NotesEvent.OnContentChange -> {
                _noteContent.value = event.content
                if (event.content.isBlank()) {
                    _isContentHintVisible.value = true
                }else {
                    _isContentHintVisible.value = false
                }
            }

            is NotesEvent.OnTitleChange -> {
                _noteTitle.value = event.title
                if (event.title.isBlank()) {
                    _isTitleHintVisible.value = true
                }else {
                    _isTitleHintVisible.value = false
                }
            }

            NotesEvent.OnBackClick -> { // call when going back from the add edit note screen
                _noteContent.value = ""
                _noteTitle.value = ""
                clickedNoteTimeStamp = 1
                _clickedNoteId.value = null
                isNote = false
                _isContentHintVisible.value = true
                _isTitleHintVisible.value = true
            }

            is NotesEvent.ChangeContentFocus -> {
//                _isTitleHintVisible.value = !event.focusState.isFocused && noteTitle.value.isBlank()
            }
            is NotesEvent.ChangeTitleFocus -> {
//                _isTitleHintVisible.value = !event.focusState.isFocused && noteContent.value.isBlank()
            }
        }
    }

    private fun getNotes() {
        getNotesJob?.cancel()
        getNotesJob = notesUseCases.getNotes()
            .onEach { notes ->
                _state.value = _state.value.copy(
                    notes = notes
                )
            }
            .launchIn(viewModelScope)

    }
}
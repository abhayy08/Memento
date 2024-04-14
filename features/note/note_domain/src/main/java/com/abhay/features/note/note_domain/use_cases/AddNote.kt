package com.abhay.features.note.note_domain.use_cases

import com.abhay.features.note.note_domain.model.InvalidNoteException
import com.abhay.features.note.note_domain.model.Note
import com.abhay.features.note.note_domain.repository.NoteRepository
import kotlin.jvm.Throws

class AddNote(
    private val repository: NoteRepository
)  {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note){
//        if(note.title.isBlank()){
//            throw InvalidNoteException("The title of the note can't be empty")
//        }
//        if(note.content.isBlank()){
//            throw InvalidNoteException("The content of the note can't be empty")
//        }
        repository.insertNote(note)
    }

}
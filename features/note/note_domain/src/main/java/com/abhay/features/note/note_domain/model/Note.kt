package com.abhay.features.note.note_domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val title: String,
    val content: String,
    val timeStamp: Long,
    @PrimaryKey val id: Int? = null
)

class InvalidNoteException(message: String) : Exception(message)
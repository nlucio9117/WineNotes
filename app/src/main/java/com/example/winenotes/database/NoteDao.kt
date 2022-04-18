package com.example.winenotes.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface NoteDao {

    @Insert
    fun addNote(note : Note) : Long

    @Query("SELECT * FROM note")
    fun getAllNotes(): List<Note>
}


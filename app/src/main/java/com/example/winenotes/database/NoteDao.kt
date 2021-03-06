package com.example.winenotes.database

import androidx.room.*


@Dao
interface NoteDao {

    /**
     * @param note the Note object to insert. The
     *      id should be 0
     *
     * @return the id of the note object just inserted
     */
    @Insert
    fun addNote(note : Note) : Long

    /**
     * @param note the Note object to update. The
     *      id CANNOT be 0
     */
    @Update
    fun updateNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    /**
     * Deletes EVERYTHING in the table
     */
    @Query("DELETE FROM note")
    fun deleteAllNotes()

    @Query("SELECT * FROM note ORDER BY title ASC")
    fun getAllNotes(): List<Note>

    @Query("SELECT * FROM note WHERE id = :noteId")
    fun getNote(noteId : Long) : Note
}


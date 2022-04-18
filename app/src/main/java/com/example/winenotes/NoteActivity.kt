package com.example.winenotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.winenotes.database.AppDatabase
import com.example.winenotes.database.Note
import com.example.winenotes.databinding.ActivityNoteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteBinding

    private var purpose: String? = ""
    private var noteId : Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = getIntent()
        purpose = intent.getStringExtra(
            getString(R.string.intent_purpose_key)
        )

        if(purpose.equals(getString(R.string.intent_purpose_update_note))) {
            noteId = intent.getLongExtra(
                getString(R.string.intent_key_note_id),
                -1
            )
            // load existing note from database
            CoroutineScope(Dispatchers.IO).launch {
                val note = AppDatabase.getDatabase(applicationContext)
                    .noteDao()
                    .getNote(noteId)

                withContext(Dispatchers.Main) {
                    binding.editTextNoteTitle.setText(note.title)
                    binding.editTextNewNote.setText(note.notes)
                }//this ends withContext
            }//this ends Coroutine
        }//this ends if statement

        setTitle("${purpose} New Note")

    }//this ends the onCreate function

    override fun onBackPressed() {
        val newNote = binding.editTextNewNote.getText().toString().trim()
        if (newNote.isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Please enter a new note", Toast.LENGTH_LONG
            ).show()
            return
        } //this ends if statement

        val title = binding.editTextNoteTitle.getText().toString().trim()
        if (title.isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Please enter a title", Toast.LENGTH_LONG
            ).show()
            return
        } //this ends if statement

        val date = binding.editTextDate.getText().toString().trim()

        CoroutineScope(Dispatchers.IO).launch {
            val noteDao = AppDatabase.getDatabase(applicationContext)
                .noteDao()

            if (purpose.equals(getString(R.string.intent_purpose_add_note))) {
                //add new note to the database
                //when adding, set primary key (id) to 0
                val note = Note(0, title, newNote, date)
                noteId = noteDao.addNote(note)
                Log.i("STATUS_NOTE", "inserted new note: ${note}")
            } else {
                //update current note in the database
                val note = Note(noteId, title, newNote, date)
                noteDao.updateNote(note)
                Log.i("STATUS", "updated existing note: ${note}")
            }//this ends the else statement

            Log.i("STATUS_NOTE", "result_id: ${noteId}")

            val intent = Intent()

            intent.putExtra(
                getString(R.string.intent_key_note_id),
                noteId
            )

            withContext(Dispatchers.Main) {
                setResult(RESULT_OK, intent)
                super.onBackPressed()
            }//this end withContext

        }//this ends the coroutine

    }//this ends onBackPressed function

}//this ends the NoteActivity
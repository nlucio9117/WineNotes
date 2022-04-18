package com.example.winenotes

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

class NoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteBinding

    private var purpose: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = getIntent()
        purpose = intent.getStringExtra(
            getString(R.string.intent_purpose_key)
        )
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

            var resultId : Long

            if (purpose.equals(getString(R.string.intent_purpose_add_note))) {
                //add new note to the database
                //when adding, set primary key (id) to 0
                val note = Note(0, title, newNote, date)
                resultId = noteDao.addNote(note)
                Log.i("STATUS_NOTE", "inserted new note: ${note}")
            } else {
                //update current note in the database
                TODO("Not implemented")
            }//this ends the else statement

        }//this ends the coroutine
        super.onBackPressed()
    }//this ends onBackPressed function

}//this ends the NoteActivity
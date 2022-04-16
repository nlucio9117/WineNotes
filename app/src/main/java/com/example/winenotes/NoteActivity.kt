package com.example.winenotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.winenotes.databinding.ActivityNoteBinding

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
        }//this ends if statement
        super.onBackPressed()
    }//this ends onBackPressed function

}//this ends the NoteActivity
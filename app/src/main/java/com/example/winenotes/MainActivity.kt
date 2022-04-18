package com.example.winenotes

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.winenotes.database.AppDatabase
import com.example.winenotes.database.Note
import com.example.winenotes.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: MyAdapter
    private val notes = mutableListOf<Note>()//mutable list that holds Note objects

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerview.setLayoutManager(layoutManager)

        val dividerItemDecoration = DividerItemDecoration(
            applicationContext, layoutManager.getOrientation()
        )
        binding.recyclerview.addItemDecoration(dividerItemDecoration)

        adapter = MyAdapter()
        binding.recyclerview.setAdapter(adapter)

        loadAllNotes()
    }//this ends the onCreate function

    private fun loadAllNotes() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val dao = db.noteDao()
            val results = dao.getAllNotes()
            for (note in results) {
                Log.i("STATUS_MAIN", "read ${note}")
            }

            withContext(Dispatchers.Main) {
                notes.clear()
                notes.addAll(results)
                adapter.notifyDataSetChanged()
            }//this ends withContext

        }//this ends CoroutineScope
    }//this ends loadAllNotes function

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }//this ends onCreateOptionsMenu

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add_newNote) {
            addNewNote()
            return true
        } else if (item.itemId == R.id.menu_sortBy_title) {
            sortByTitle()
            return true
        } else if (item.itemId == R.id.menu_sortBy_date) {
            sortByDate()
            return true
        }//this ends final else if statement
        return super.onOptionsItemSelected(item)
    }//this ends onOptionsItemSelected

    private fun sortByDate() {
        TODO("Not yet implemented")
    }

    private fun sortByTitle() {
        TODO("Not yet implemented")
    }

    private val startForAddResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result : ActivityResult ->

            if (result.resultCode == Activity.RESULT_OK) {
                loadAllNotes()
            }//this ends if statement
        }//this ends registerForActivityResult

    private val startForUpdateResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result : ActivityResult ->

            if (result.resultCode == Activity.RESULT_OK) {
                //reload the whole database
                // good only for small databases
                loadAllNotes()
            }//this ends if statement
        }//this ends registerForActivityResult

    private fun addNewNote() {
        val intent = Intent(applicationContext, NoteActivity::class.java)
        intent.putExtra(
            getString(R.string.intent_purpose_key),
            getString(R.string.intent_purpose_add_note)
        )
        startForAddResult.launch(intent)
    }//this ends function addNewNote

    inner class MyViewHolder(val view: TextView) :
        RecyclerView.ViewHolder(view),
        View.OnClickListener, View.OnLongClickListener {

        init {
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
        }

        override fun onClick(view: View?) {
            val intent = Intent(applicationContext, NoteActivity::class.java)

            intent.putExtra(
                getString(R.string.intent_purpose_key),
                getString(R.string.intent_purpose_update_note)
            )

            val note = notes[adapterPosition]
            intent.putExtra(
                getString(R.string.intent_key_note_id),
                note.id
            )
            startForUpdateResult.launch(intent)
        }//this ends onClick function

        override fun onLongClick(view: View?): Boolean {

            val note = notes[adapterPosition]
            val builder = AlertDialog.Builder(view!!.context)
                .setTitle("Confirm delete")
                .setMessage("Are you sure you want to delete: " +
                "${note.title} ${note.notes}?")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok) {
                    dialogInterface, whichButton ->

                    CoroutineScope(Dispatchers.IO).launch {
                        AppDatabase.getDatabase(applicationContext)
                            .noteDao()
                            .deleteNote(note)

                        // reload the whole database
                        // good only for small databases
                        loadAllNotes()
                    }
                }
            builder.show()

            return true
        }//this ends onLongClick function


    }//this ends MyViewHolder class


    inner class MyAdapter :
        RecyclerView.Adapter<MyViewHolder>() {

        //loads item_view layout and ties it to a viewholder
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_view, parent, false) as TextView
            return MyViewHolder(view)
        }//this ends onCreateViewHolder

        //updates data inside viewholder
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val note = notes[position]
            holder.view.setText("${note.title}: ${note.notes}")
        }//this ends onBindViewHolder

        //returns the size of data
        override fun getItemCount(): Int {
            return notes.size
        }//this ends getItemCount

    }//this ends the MyAdapter inner class


}//this ends the entire MainActivity
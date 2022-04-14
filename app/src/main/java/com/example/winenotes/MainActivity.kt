package com.example.winenotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
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

            withContext(Dispatchers.Main) {
                notes.clear()
                notes.addAll(results)
                adapter.notifyDataSetChanged()
            }//this ends withContext

        }//this ends CoroutineScope
    }//this ends loadAllNotes function

    inner class MyViewHolder(val view: TextView) :
        RecyclerView.ViewHolder(view) {

        }//this ends the MyViewHolder inner class

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
            holder.view.text = notes[position].toString()
        }//this ends onBindViewHolder

        //returns the size of data
        override fun getItemCount(): Int {
            return notes.size
        }//this ends getItemCount

    }//this ends the MyAdapter inner class


}//this ends the entire MainActivity
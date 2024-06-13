package com.example.myapplication.usuarisrv

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class SaveTerea : AppCompatActivity() {

    private lateinit var textViewTitle: TextView
    private lateinit var textViewDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_tarea)

        // Initialize TextViews
        textViewTitle = findViewById(R.id.textViewTitle)
        textViewDescription = findViewById(R.id.textViewDescription)

        // Get the values passed from the previous activity
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")

        // Set the values to TextViews
        textViewTitle.text = title
        textViewDescription.text = description
    }
}


package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity7 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main7)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main7)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val nextButton = findViewById<ImageView>(R.id.addButton3)
        nextButton.setOnClickListener {
            // Create an Intent to start StartPage2 activity
            val intent = Intent(this, MainActivity8::class.java)
            startActivity(intent)
        }
        val nextButton4 = findViewById<TextView>(R.id.taskk)
        nextButton4.setOnClickListener {
            // Create an Intent to start StartPage2 activity
            val intent = Intent(this, MainActivity7::class.java)
            startActivity(intent)
        }
        val nextButton5 = findViewById<TextView>(R.id.home)
        nextButton5.setOnClickListener {
            // Create an Intent to start StartPage2 activity
            val intent = Intent(this, MainActivity6::class.java)
            startActivity(intent)
        }
        val nextButton6 = findViewById<TextView>(R.id.profile33)
        nextButton6.setOnClickListener {
            // Create an Intent to start StartPage2 activity
            val intent = Intent(this, MainActivity9::class.java)
            startActivity(intent)
        }
    }
}
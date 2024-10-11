package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity6 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main6)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main6)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val nextButton = findViewById<TextView>(R.id.Reminder1)
        nextButton.setOnClickListener {
            // Create an Intent to start StartPage2 activity
            val intent = Intent(this, MainActivity8::class.java)
            startActivity(intent)
        }
        val nextButton3 = findViewById<TextView>(R.id.profile1)
        nextButton3.setOnClickListener {
            // Create an Intent to start StartPage2 activity
            val intent = Intent(this, MainActivity9::class.java)
            startActivity(intent)
        }



        val nextButton7 = findViewById<TextView>(R.id.today)
        nextButton7.setOnClickListener {
            // Create an Intent to start StartPage2 activity
            val intent = Intent(this, MainActivity8::class.java)
            startActivity(intent)
        }
        val nextButton8 = findViewById<TextView>(R.id.Scheduled)
        nextButton8.setOnClickListener {
            // Create an Intent to start StartPage2 activity
            val intent = Intent(this, MainActivity8::class.java)
            startActivity(intent)
        }
        val nextButton9 = findViewById<TextView>(R.id.All)
        nextButton9.setOnClickListener {
            // Create an Intent to start StartPage2 activity
            val intent = Intent(this, MainActivity8::class.java)
            startActivity(intent)
        }


    }
}
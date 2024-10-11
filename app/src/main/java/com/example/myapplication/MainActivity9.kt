package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity9 : AppCompatActivity() {
    private lateinit var timePicker: TimePicker
    private lateinit var taskEditText: EditText
    private lateinit var alarmDisplayTextView: TextView // TextView to display set alarms
    private val alarmList = mutableListOf<String>() // List to store alarms

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main9)

        taskEditText = findViewById(R.id.taskEditText)
        timePicker = findViewById(R.id.timePicker)
        alarmDisplayTextView = findViewById(R.id.alarmDisplayTextView) // Initialize TextView

        // Set TimePicker to 24-hour view (optional)
        timePicker.setIs24HourView(true)

        // Set alarm button
        val setAlarmButton = findViewById<Button>(R.id.setAlarmButton)
        setAlarmButton.setOnClickListener {
            val task = taskEditText.text.toString()
            if (task.isNotEmpty()) {
                try {
                    setAlarm(task)
                } catch (e: Exception) {
                    Log.e("AlarmError", "Error setting alarm", e)
                    Toast.makeText(this, "Failed to set alarm", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter a task", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigation back to home
        val nextButton5 = findViewById<TextView>(R.id.home)
        nextButton5.setOnClickListener {
            val intent = Intent(this, MainActivity6::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm(task: String) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
        calendar.set(Calendar.MINUTE, timePicker.minute)
        calendar.set(Calendar.SECOND, 0)

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("task", task)

        // Use correct PendingIntent flags based on Android version
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        // Display the set alarm
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeString = timeFormat.format(calendar.time)
        val alarmInfo = "Task: $task at $timeString"

        // Update the alarm list and display
        alarmList.add(alarmInfo)
        updateAlarmDisplay()

        // Confirm the alarm is set
        Toast.makeText(this, "Alarm set for: $task", Toast.LENGTH_SHORT).show()
        Log.d("AlarmDebug", "Alarm set for task: $task at ${calendar.time}")
    }

    // Function to update the alarm display TextView
    private fun updateAlarmDisplay() {
        alarmDisplayTextView.text = alarmList.joinToString(separator = "\n")
    }
}

package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.UUID
import java.util.concurrent.TimeUnit

// Main activity class implementing task management with notifications and timers
class MainActivity8 : AppCompatActivity(), TaskAdapter.TaskItemListener {

    // UI elements
    private lateinit var taskEditText: EditText
    private lateinit var addTaskButton: ImageView
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var bottomNavigationView: BottomNavigationView
    private val tasks = mutableListOf<Task>() // List to store tasks
    private val timers = mutableMapOf<String, CountDownTimer>() // Timers mapped to task IDs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main8)

        // Initialize UI elements
        taskEditText = findViewById(R.id.taskEditText)
        addTaskButton = findViewById(R.id.addTaskButton)
        taskRecyclerView = findViewById(R.id.taskRecyclerView)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Load existing tasks from storage
        loadTasks()

        // Set up RecyclerView with tasks and adapter
        taskAdapter = TaskAdapter(tasks, this)
        taskRecyclerView.adapter = taskAdapter
        taskRecyclerView.layoutManager = LinearLayoutManager(this)

        // Add new task when button is clicked
        addTaskButton.setOnClickListener {
            addTask()
        }

        // Set up navigation menu
        setupNavigation()

        // Create notification channel for task notifications
        createNotificationChannel()

        // Button listeners for navigating between screens
        val nextButton4 = findViewById<TextView>(R.id.taskk)
        nextButton4.setOnClickListener {
            val intent = Intent(this, MainActivity7::class.java)
            startActivity(intent)
        }
        val nextButton5 = findViewById<TextView>(R.id.home)
        nextButton5.setOnClickListener {
            val intent = Intent(this, MainActivity6::class.java)
            startActivity(intent)
        }
        val nextButton6 = findViewById<TextView>(R.id.profile33)
        nextButton6.setOnClickListener {
            val intent = Intent(this, MainActivity9::class.java)
            startActivity(intent)
        }
    }

    // Function to add a new task
    private fun addTask() {
        val taskText = taskEditText.text.toString().trim()
        if (taskText.isNotEmpty()) {
            // Show time picker dialog for task duration
            showTimePickerDialog { hours, minutes ->
                val durationInMillis = (hours * 3600 + minutes * 60) * 1000L
                val task = Task(UUID.randomUUID().toString(), taskText, durationInMillis)
                tasks.add(task) // Add task to list
                taskAdapter.notifyItemInserted(tasks.size - 1)
                taskEditText.text.clear() // Clear input field
                saveTasks() // Save updated task list
            }
        }
    }

    // Show a time picker dialog to set task duration
    private fun showTimePickerDialog(callback: (Int, Int) -> Unit) {
        TimePickerDialog(this, { _, hourOfDay, minute ->
            callback(hourOfDay, minute)
        }, 0, 0, true).show()
    }

    // Save tasks to shared preferences
    private fun saveTasks() {
        val sharedPref = getSharedPreferences("com.example.myapplication.TASK_PREFERENCES", Context.MODE_PRIVATE)
        val taskSet = tasks.map { "${it.id},${it.text},${it.duration},${it.isCompleted}" }.toSet()
        with(sharedPref.edit()) {
            putStringSet("tasks", taskSet) // Save task data as a set
            apply()
        }
        updateWidget() // Update task widget after saving
    }

    // Update the task widget
    private fun updateWidget() {
        val intent = Intent(this, TaskWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(ComponentName(application, TaskWidgetProvider::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent) // Notify widget to update
    }

    // Load tasks from shared preferences
    private fun loadTasks() {
        val sharedPref = getSharedPreferences("com.example.myapplication.TASK_PREFERENCES", Context.MODE_PRIVATE)
        val savedTasks = sharedPref.getStringSet("tasks", setOf()) ?: setOf()
        tasks.clear() // Clear current tasks

        // Parse and add each saved task
        savedTasks.forEach { savedTask ->
            val parts = savedTask.split(",")
            if (parts.size == 4) {
                try {
                    val id = parts[0]
                    val text = parts[1]
                    val duration = parts[2].toLong()
                    val isCompleted = parts[3].toBoolean()
                    tasks.add(Task(id, text, duration, isCompleted))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                println("Malformed task data: $savedTask")
            }
        }
    }

    // Set up navigation menu behavior
    private fun setupNavigation() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.taskk -> true // Already on this screen
                R.id.home -> {
                    startActivity(Intent(this, MainActivity6::class.java))
                    true
                }
                R.id.profile33 -> {
                    startActivity(Intent(this, MainActivity9::class.java))
                    true
                }
                else -> false
            }
        }
    }

    // Edit an existing task
    override fun onEditTask(position: Int) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.edit_task_dialog, null)
        val editText = dialogView.findViewById<EditText>(R.id.editTaskEditText)

        editText.setText(tasks[position].text) // Set current task text

        builder.setView(dialogView)
            .setTitle("Edit Task")
            .setPositiveButton("Save") { _, _ ->
                val editedTask = editText.text.toString().trim()
                if (editedTask.isNotEmpty()) {
                    // Show time picker dialog to set new task duration
                    showTimePickerDialog { hours, minutes ->
                        val newDurationInMillis = (hours * 3600 + minutes * 60) * 1000L
                        tasks[position] = tasks[position].copy(
                            text = editedTask,
                            duration = newDurationInMillis,  // Update the duration
                            remainingTime = newDurationInMillis  // Reset the remaining time
                        )
                        taskAdapter.notifyItemChanged(position) // Notify adapter of changes
                        saveTasks() // Save updated task list
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Delete an existing task
    override fun onDeleteTask(position: Int) {
        val task = tasks[position]
        timers[task.id]?.cancel() // Cancel any active timer for the task
        timers.remove(task.id) // Remove the timer from the map
        tasks.removeAt(position) // Remove task from the list
        taskAdapter.notifyItemRemoved(position) // Notify adapter of task removal
        saveTasks() // Save updated task list
    }

    // Start the countdown timer for a task
    override fun onStartTimer(position: Int) {
        val task = tasks[position]
        if (!task.isCompleted) {
            // Start a countdown timer for the task
            val timer = object : CountDownTimer(task.duration, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    task.remainingTime = millisUntilFinished
                    taskAdapter.notifyItemChanged(position) // Update task progress
                }

                override fun onFinish() {
                    task.isCompleted = true
                    task.remainingTime = 0
                    taskAdapter.notifyItemChanged(position)
                    showNotification(task.text) // Notify user of task completion
                    timers.remove(task.id)
                    saveTasks() // Save updated task list
                }
            }
            timer.start() // Start the timer
            timers[task.id] = timer // Save the timer in the map
            taskAdapter.notifyItemChanged(position)
        }
    }

    // Create notification channel for task notifications
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("TASK_CHANNEL", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Show notification when a task is completed
    private fun showNotification(taskText: String) {
        val intent = Intent(this, MainActivity8::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, "TASK_CHANNEL")
            .setSmallIcon(R.drawable.icons8_plus_24)
            .setContentTitle("Task Completed")
            .setContentText("The task '$taskText' has been completed!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(taskText.hashCode(), builder.build()) // Show notification
    }

    // Stop all active timers when the activity is stopped
    override fun onStop() {
        super.onStop()
        timers.values.forEach { it.cancel() } // Cancel all active timers
        timers.clear() // Clear timer map
    }
}

// Data class to represent a Task
data class Task(
    val id: String, // Unique identifier for the task
    val text: String, // Task description
    val duration: Long, // Task duration in milliseconds
    var isCompleted: Boolean = false, // Whether the task is completed
    var remainingTime: Long = duration // Remaining time for the task
)

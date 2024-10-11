// TaskAdapter.kt
package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.TimeUnit

class TaskAdapter(
    private val tasks: List<Task>,         // List of tasks to be displayed
    private val listener: TaskItemListener // Listener interface for task interactions
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // Define the interface to handle task actions
    interface TaskItemListener {
        fun onEditTask(position: Int)       // Called when edit button is clicked
        fun onDeleteTask(position: Int)     // Called when delete button is clicked
        fun onStartTimer(position: Int)     // Called when start timer button is clicked
    }

    // ViewHolder class that represents a single task item in the RecyclerView
    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskText: TextView = view.findViewById(R.id.taskTextView)
        val taskDuration: TextView = view.findViewById(R.id.taskDuration)
        val startTimerButton: Button = view.findViewById(R.id.startTimerButton)
        val editButton: View = view.findViewById(R.id.editTaskButton)
        val deleteButton: View = view.findViewById(R.id.deleteTaskButton)
    }

    // Inflate the task item layout and return a TaskViewHolder instance
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    // Format time for displaying in HH:mm:ss format
    private fun formatTime(timeInMillis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(timeInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    // Bind task data to the ViewHolder (position in the list)
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]          // Get the task for the current position
        holder.taskText.text = task.text    // Set the task description

        // Use the helper function to format and display the remaining time
        holder.taskDuration.text = formatTime(task.remainingTime)

        // Hide the Edit button if the task is completed
        if (task.isCompleted) {
            holder.editButton.visibility = View.GONE
        } else {
            holder.editButton.visibility = View.VISIBLE
        }

        if (!task.isCompleted) {            // If the task is not completed, display the remaining time
            holder.startTimerButton.visibility = View.VISIBLE
            holder.startTimerButton.setOnClickListener { listener.onStartTimer(position) }
        } else {
            // If task is completed, show "Completed" text and hide the timer button
            holder.taskDuration.text = "Completed"
            holder.startTimerButton.visibility = View.GONE
        }

        // Set the click listeners for edit and delete buttons
        holder.editButton.setOnClickListener { listener.onEditTask(position) }
        holder.deleteButton.setOnClickListener { listener.onDeleteTask(position) }
    }

    // Return the total number of tasks
    override fun getItemCount() = tasks.size
}

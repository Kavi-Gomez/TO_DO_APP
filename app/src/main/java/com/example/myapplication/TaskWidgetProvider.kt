// TaskWidgetProvider.kt
package com.example.myapplication

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName  // Import ComponentName
import android.content.Context
import android.widget.RemoteViews
import android.content.SharedPreferences
import android.app.PendingIntent
import android.content.Intent

class TaskWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.task_widget)

            // Get the top 3 upcoming tasks
            val tasks = getUpcomingTasks(context)

            // Update the widget views with task information
            for (i in 0 until minOf(3, tasks.size)) {
                val taskViewId = when (i) {
                    0 -> R.id.widget_task_1
                    1 -> R.id.widget_task_2
                    else -> R.id.widget_task_3
                }
                views.setTextViewText(taskViewId, tasks[i].text)
            }

            // Clear remaining views if tasks are less than 3
            if (tasks.size < 3) {
                for (i in tasks.size until 3) {
                    val taskViewId = when (i) {
                        0 -> R.id.widget_task_1
                        1 -> R.id.widget_task_2
                        else -> R.id.widget_task_3
                    }
                    views.setTextViewText(taskViewId, "")
                }
            }

            // Create an Intent to launch the MainActivity8 when the widget is clicked
            val intent = Intent(context, MainActivity8::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun getUpcomingTasks(context: Context): List<Task> {
            val sharedPref: SharedPreferences = context.getSharedPreferences("com.example.myapplication.TASK_PREFERENCES", Context.MODE_PRIVATE)
            val savedTasks = sharedPref.getStringSet("tasks", setOf()) ?: setOf()

            return savedTasks.mapNotNull { savedTask ->
                val parts = savedTask.split(",")
                if (parts.size == 4) {
                    try {
                        Task(parts[0], parts[1], parts[2].toLong(), parts[3].toBoolean())
                    } catch (e: Exception) {
                        null
                    }
                } else {
                    null
                }
            }.filter { !it.isCompleted }.sortedBy { it.duration }.take(3)
        }

        // Function to trigger widget updates manually
        fun notifyWidgetUpdate(context: Context) {
            val intent = Intent(context, TaskWidgetProvider::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            val ids = AppWidgetManager.getInstance(context).getAppWidgetIds(
                ComponentName(context, TaskWidgetProvider::class.java)
            )
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            context.sendBroadcast(intent)
        }
    }
}

//TimerService.kt
package com.example.myapplication

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.concurrent.TimeUnit

class TimeService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val taskId = intent?.getStringExtra("taskId") ?: return START_NOT_STICKY
        Log.d("TimeService", "Timer started for task ID: $taskId")
        // Implement timer logic here
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

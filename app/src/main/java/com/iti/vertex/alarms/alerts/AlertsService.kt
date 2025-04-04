package com.iti.vertex.alarms.alerts

import android.app.Service
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.iti.vertex.MainActivity
import com.iti.vertex.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

private const val TAG = "AlertsService"
class AlertsService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View
    private lateinit var mediaPlayer: MediaPlayer
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WindowManager::class.java)
        overlayView = LayoutInflater.from(this).inflate(R.layout.alert_layout, null)


        val layoutParameters = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        layoutParameters.gravity = Gravity.TOP

        mediaPlayer = MediaPlayer.create(this, R.raw.gym)
        mediaPlayer.start()

        windowManager.addView(overlayView, layoutParameters)

        val tvDesc = overlayView.findViewById<TextView>(R.id.tvDesc)
        val btnDismiss = overlayView.findViewById<Button>(R.id.btnDismiss)
        val btnOpen = overlayView.findViewById<Button>(R.id.btnOpen)


        tvDesc.text = "Static values"

        btnDismiss.setOnClickListener {
            Log.i(TAG, "onCreate: clicked on Dismiss button")
            stopSelf()
        }
        btnOpen.setOnClickListener { v ->
            Log.i(TAG, "onCreate: clicked on open button")
            Intent(this, MainActivity::class.java).also {
                it.flags = FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
            }
            stopSelf()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand: ")
        val message = intent?.getStringExtra("DESC") ?: "NOT FOUND"
        overlayView.findViewById<TextView>(R.id.tvDesc).text = message
        return START_NOT_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(overlayView)
        mediaPlayer.release()
        job.cancel()
    }

}
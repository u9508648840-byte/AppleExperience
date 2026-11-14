package com.apple.experience

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import android.graphics.Color
import android.graphics.drawable.GradientDrawable

class DynamicIslandService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var islandView: TextView

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        islandView = TextView(this).apply {
            text = "  Dynamic Island  "
            setTextColor(Color.WHITE)
            textSize = 12f
            val drawable = GradientDrawable().apply { setColor(Color.BLACK); cornerRadius = 60f }
            background = drawable
        }
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        params.y = 20 // Posizione per il foro del Moto G23
        windowManager.addView(islandView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::islandView.isInitialized) windowManager.removeView(islandView)
    }
    override fun onBind(intent: Intent?): IBinder? = null
}

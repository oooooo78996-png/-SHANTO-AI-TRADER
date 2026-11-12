package com.shanto.trader

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import java.util.Locale

class FloatingWidgetService : Service(), TextToSpeech.OnInitListener {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate() {
        super.onCreate()
        textToSpeech = TextToSpeech(this, this)
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // ফ্লোটিং লেআউট ইনফ্লেট করা
        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_widget_layout, null)

        val LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else
            WindowManager.LayoutParams.TYPE_PHONE

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.START
        params.x = 100
        params.y = 200

        windowManager.addView(floatingView, params)

        // ভাসমান লোগো বা আইকনে ক্লিক করলে সিগন্যাল ও ভয়েস এনালাইসিস হবে
        val btnFloatingLogo = floatingView.findViewById<Button>(R.id.btnFloatingLogo)
        btnFloatingLogo.setOnClickListener {
            analyzeAndSpeakSignal()
        }
    }

    private fun analyzeAndSpeakSignal() {
        speakOut("মার্কেট স্ক্যান করা হচ্ছে...")

        // ৩ সেকেন্ড পর রিয়েল সিগন্যাল জেনারেট করে ভয়েস ও স্ক্রিনে বলে দেওয়া
        android.os.Handler(mainLooper).postDelayed({
            val signals = listOf("UP", "DOWN", "NO TRADE")
            val chosenSignal = signals.random() // এখানে আপনার এনালাইসিস লজিক বসবে

            when (chosenSignal) {
                "UP" -> {
                    speakOut("আপ ট্রেড নিন!")
                    Toast.makeText(this, "Signal: UP", Toast.LENGTH_SHORT).show()
                }
                "DOWN" -> {
                    speakOut("ডাউন ট্রেড নিন!")
                    Toast.makeText(this, "Signal: DOWN", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    speakOut("কোনো ট্রেড নেই, অপেক্ষা করুন।")
                    Toast.makeText(this, "Signal: NO TRADE", Toast.LENGTH_SHORT).show()
                }
            }
        }, 2500)
    }

    private fun speakOut(text: String) {
        if (::textToSpeech.isInitialized) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale("bn", "BD")) // বাংলা ভয়েস
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        if (::floatingView.isInitialized) {
            windowManager.removeView(floatingView)
        }
    }
}

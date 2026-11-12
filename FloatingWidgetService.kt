package com.shanto.trader // আপনার প্রোজেক্টের প্যাকেজ নেম অনুযায়ী এটি দেবেন

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

        // ফ্লোটিং আইকনে ক্লিক করলে স্ক্যান ও ভয়েস সিগন্যাল দেবে
        val btnFloatingAI = floatingView.findViewById<Button>(R.id.btnFloatingAI)
        btnFloatingAI.setOnClickListener {
            analyzeMarketAndSpeak()
        }
    }

    private fun analyzeMarketAndSpeak() {
        speakOut("মার্কেট স্ক্যান করা হচ্ছে... দয়া করে অপেক্ষা করুন।")
        Toast.makeText(this, "AI Scanning Market...", Toast.LENGTH_SHORT).show()

        android.os.Handler(mainLooper).postDelayed({
            val signals = listOf("UP TRADE", "DOWN TRADE")
            val selectedSignal = signals.random()

            if (selectedSignal == "UP TRADE") {
                speakOut("সিগন্যাল পাওয়া গেছে! আপ ট্রেড নিন।")
            } else {
                speakOut("সিগন্যাল পাওয়া গেছে! ডাউন ট্রেড নিন।")
            }
        }, 3000)
    }

    private fun speakOut(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale("bn", "BD"))
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        if (::floatingView.isInitialized) {
            windowManager.removeView(floatingView)
        }
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }
}

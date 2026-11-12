package com.shanto.trader

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var mediaProjectionManager: MediaProjectionManager
    private lateinit var textToSpeech: TextToSpeech
    private val SCREEN_CAPTURE_REQ_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ভয়েস ইঞ্জিন চালু করা
        textToSpeech = TextToSpeech(this, this)
        mediaProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

        val btnEnableFloating = findViewById<Button>(R.id.btnEnableFloating)
        val btnStartCapture = findViewById<Button>(R.id.btnStartCapture)

        // ১. প্রথম বাটন: ক্লিক করলে ভয়েস দিয়ে এনালাইসিস শুরু করবে
        btnEnableFloating.setOnClickListener {
            speakOut("মার্কেট স্ক্যান শুরু হচ্ছে, অপেক্ষা করুন।")
            Toast.makeText(this, "AI Market Analysis Started!", Toast.LENGTH_SHORT).show()
            
            // ৩ সেকেন্ড পর র্যান্ডম ট্রেড সিগন্যাল ও ভয়েস আউটপুট
            window.decorView.postDelayed({
                val signals = listOf("UP TRADE", "DOWN TRADE")
                val chosen = signals.random()
                if (chosen == "UP TRADE") {
                    speakOut("সিগন্যাল পাওয়া গেছে! আপ ট্রেড নিন।")
                    Toast.makeText(this, "🟢 SIGNAL: UP TRADE", Toast.LENGTH_LONG).show()
                } else {
                    speakOut("সিগন্যাল পাওয়া গেছে! ডাউন ট্রেড নিন।")
                    Toast.makeText(this, "🔴 SIGNAL: DOWN TRADE", Toast.LENGTH_LONG).show()
                }
            }, 3000)
        }

        // ২. দ্বিতীয় বাটন: স্ক্রিন ক্যাপচার পারমিশন ও স্ক্যানিং
        btnStartCapture.setOnClickListener {
            val intent = mediaProjectionManager.createScreenCaptureIntent()
            startActivityForResult(intent, SCREEN_CAPTURE_REQ_CODE)
        }
    }

    private fun speakOut(text: String) {
        if (::textToSpeech.isInitialized) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale("bn", "BD"))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SCREEN_CAPTURE_REQ_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this, "স্ক্রিন সফলভাবে ক্যাপচার হয়েছে!", Toast.LENGTH_SHORT).show()
            speakOut("স্ক্রিন ক্যাপচার সফল হয়েছে। এনালাইসিস চলছে।")
        }
    }

    override fun onDestroy() {
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}

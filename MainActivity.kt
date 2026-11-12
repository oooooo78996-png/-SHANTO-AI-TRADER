package com.shanto.trader // আপনার প্রোজেক্টের প্যাকেজ নেম অনুযায়ী এটি ঠিক রাখবেন

import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mediaProjectionManager: MediaProjectionManager
    private val SCREEN_CAPTURE_REQ_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

        val btnEnableFloating = findViewById<Button>(R.id.btnEnableFloating)
        val btnStartCapture = findViewById<Button>(R.id.btnStartCapture)

        // ১. ফ্লোটিং বাটন সার্ভিস চালু করা
        btnEnableFloating.setOnClickListener {
            val intent = Intent(this, FloatingWidgetService::class.java)
            startService(intent)
            Toast.makeText(this, "Floating AI Button Activated!", Toast.LENGTH_SHORT).show()
        }

        // ২. স্ক্রিন ক্যাপচার ও পারমিশন চাওয়া
        btnStartCapture.setOnClickListener {
            val captureIntent = mediaProjectionManager.createScreenCaptureIntent()
            startActivityForResult(captureIntent, SCREEN_CAPTURE_REQ_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SCREEN_CAPTURE_REQ_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Screen Capture Permitted. AI Ready!", Toast.LENGTH_SHORT).show()
            // এখানে স্ক্রিন ডেটা সার্ভিস বা প্রসেসরে পাঠানো যাবে
        }
    }
}

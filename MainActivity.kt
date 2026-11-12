package com.shanto.trader // আপনার প্রোজেক্টের প্যাকেজ নেম অনুযায়ী এটি ঠিক রাখবেন

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnEnableFloating = findViewById<Button>(R.id.btnEnableFloating)
        val btnStartCapture = findViewById<Button>(R.id.btnStartCapture)

        // ১. ফ্লোটিং বাটন এনাবল ও পারমিশন চেক
        btnEnableFloating.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.CanDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, 2084)
                Toast.makeText(this, "দয়া করে ফ্লোটিং পারমিশন অন করুন", Toast.LENGTH_LONG).show()
            } else {
                val serviceIntent = Intent(this, FloatingWidgetService::class.java)
                startService(serviceIntent)
                Toast.makeText(this, "Floating AI Button Activated!", Toast.LENGTH_SHORT).show()
            }
        }

        // ২. স্ক্রিন ক্যাপচার ও ট্রেডিং মোড রেডি করা
        btnStartCapture.setOnClickListener {
            Toast.makeText(this, "AI Trading Mode Ready. Use Floating Button!", Toast.LENGTH_SHORT).show()
        }
    }
}

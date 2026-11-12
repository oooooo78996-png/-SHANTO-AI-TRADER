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
        
        // ভয়েস (TTS) ইনিশিয়ালাইজেশন
        textToSpeech = TextToSpeech(this, this)

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
        // ফ্লোটিং লেআউট ইনফ্লেট করা
        // (আপনার প্রজেক্টের floating_layout.xml অনুযায়ী)
        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_widget_layout, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.START
        params.x = 100
        params.y = 100

        windowManager.addView(floatingView, params)

        // ফ্লোটিং বাটনে ক্লিক করার মূল ইভেন্ট
        val btnFloatingAI = floatingView.findViewById<Button>(R.id.btnFloatingAI)
        btnFloatingAI.setOnClickListener {
            startAiMarketScanning()
        }
    }

    private fun startAiMarketScanning() {
        // ১. ভয়েস ও টোস্টের মাধ্যমে এনালাইসিস শুরুর ঘোষণা
        speakOut("मार्केट স্ক্যান করা হচ্ছে... দয়া করে অপেক্ষা করুন।")
        Toast.makeText(this, "AI Scanning Market...", Toast.LENGTH_SHORT).show()

        // ২. এখানে স্ক্রিন ক্যাপচার করে ক্যান্ডেলস্টিক বা ট্রেন্ড এনালাইসিস হবে
        // (উদাহরণস্বরূপ লজিক বা রেন্ডম সিগন্যাল জেনারেটর)
        android.os.Handler(mainLooper).postDelayed({
            // এনালাইসিস রেজাল্ট (UP অথবা DOWN)
            val signals = listOf("UP TRADE", "DOWN TRADE", "NO SIGNAL")
            val selectedSignal = signals.random() // আপনার আসল AI লজিক এখানে বসবে

            if (selectedSignal == "UP TRADE") {
                speakOut("সিগন্যাল পাওয়া গেছে! আপ ট্রেড নিন।")
                sendTelegramAlert("🟢 SHANTO AI SIGNAL: UP TRADE")
            } else if (selectedSignal == "DOWN TRADE") {
                speakOut("সিগন্যাল পাওয়া গেছে! ডাউন ট্রেড নিন।")
                sendTelegramAlert("🔴 SHANTO AI SIGNAL: DOWN TRADE")
            } else {
                speakOut("বর্তমানে কোনো সিগন্যাল নেই, অপেক্ষা করুন।")
            }
        }, 3000) // ৩ সেকেন্ড স্ক্যানিং ডিলে
    }

    private fun speakOut(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun sendTelegramAlert(message: String) {
        // আপনার টেলিগ্রাম বটের মাধ্যমে নোটিফিকেশন পাঠানোর কোড এখানে কাজ করবে
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale("bn", "BD")) // বাংলা ভাষা সেটআপ
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

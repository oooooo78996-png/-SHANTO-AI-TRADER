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
        setContentView(R.layout.activity_main) // আপনার XML लेआउट

        mediaProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

        val btnEnableFloating = findViewById<Button>(R.id.btnEnableFloating)
        val btnStartCapture = findViewById<Button>(R.id.btnStartCapture)

        // ১. ফ্লোটিং বাটন এনাবল করার কাজ
        btnEnableFloating.setOnClickListener {
            Toast.makeText(this, "Floating AI Button Enabled!", Toast.LENGTH_SHORT).show()
            // ফ্লোটিং উইন্ডো সার্ভিস এখানে স্টার্ট হবে
            val serviceIntent = Intent(this, FloatingWidgetService::class.java)
            startService(serviceIntent)
        }

        // ২. স্ক্রিন ক্যাপচার ও এআই স্ক্যানিং শুরু করার কাজ
        btnStartCapture.setOnClickListener {
            startScreenCapture()
        }
    }

    private fun startScreenCapture() {
        // ভিডিওর মতো অ্যানাউন্সমেন্ট ও স্ক্যানিং শুরু
        Toast.makeText(this, "ট্রেডিং চার্ট স্ক্যান করা হচ্ছে...", Toast.LENGTH_SHORT).show()
        
        // Android Screen Capture Permission Intent
        val intent = mediaProjectionManager.createScreenCaptureIntent()
        startActivityForResult(intent, SCREEN_CAPTURE_REQ_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SCREEN_CAPTURE_REQ_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this, "ট্রেডিং চার্ট পাওয়া গেছে, এনালাইসিস চলছে...", Toast.LENGTH_SHORT).show()
            
            // এখানে স্ক্রিনশট প্রসেস হয়ে AI Analysis (UP / DOWN / NO SIGNAL) বের হবে
            // এবং আপনার ভয়েস বা টেলিগ্রামে সিগন্যাল চলে যাবে।
            runAiMarketAnalysis(data!!)
        }
    }

    private fun runAiMarketAnalysis(screenData: Intent) {
        // ফ্লোটিং সার্ভিস বা ব্যাকগ্রাউন্ডে স্ক্রিন ইমেজ প্রসেস করার কোড
        // এনালাইসিস শেষ হলে সিগন্যাল ডিক্লেয়ার করবে (যেমন: UP TRADE বা DOWN TRADE)
    }
}

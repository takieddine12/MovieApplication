package taki.eddine.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.ybq.android.spinkit.style.Circle
import kotlinx.android.synthetic.main.activity_favdetails.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import maes.tech.intentanim.CustomIntent.customType
import taki.eddine.myapplication.databinding.ActivitySplashScreenBinding
import taki.eddine.myapplication.ui.MainActivity

class SplashScreen : AppCompatActivity() {
    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            Intent(this@SplashScreen, MainActivity::class.java).apply {
                startActivity(this)
                finish()
                customType(this@SplashScreen,"fadein-to-fadeout")
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
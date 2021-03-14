package taki.eddine.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import taki.eddine.myapplication.R
import taki.eddine.myapplication.databinding.ActivityReadMoreBinding

class ReadMoreActivity : AppCompatActivity() {
    private var _binding : ActivityReadMoreBinding?  = null
    private val binding get() = _binding !!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityReadMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val intent = intent
        intent?.let {
            val review = it.getStringExtra("review")
            val createdAt = it.getStringExtra("createdAt")
            val updatedAt = it.getStringExtra("updateAt")

            binding.content.text  = review
            (getString(R.string.createdAt).plus(" : ") + createdAt).also { binding.createdAt.text = it }
            (getString(R.string.updatedAt).plus(" : ") + updatedAt).also { binding.updateAt.text = it }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
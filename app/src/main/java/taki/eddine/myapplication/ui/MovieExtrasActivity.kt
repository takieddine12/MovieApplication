package taki.eddine.myapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint
import taki.eddine.myapplication.fragments.CreditsFragment
import taki.eddine.myapplication.fragments.RecommendationsFragment
import taki.eddine.myapplication.fragments.ReviewsFragment
import taki.eddine.myapplication.R
import taki.eddine.myapplication.databinding.ActivityMovieExtrasBinding
import taki.eddine.myapplication.mvvm.SharedViewModel
import timber.log.Timber

@AndroidEntryPoint
class MovieExtrasActivity : AppCompatActivity() {
    private var sharedViewModel: SharedViewModel? = null
    private var binding: ActivityMovieExtrasBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieExtrasBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        val intent = intent
        intent?.let {
            val movieId = intent.getStringExtra("movieId")
            if (savedInstanceState == null) {
                binding!!.label.setText(R.string.MoviesReviews)
                sharedViewModel!!.setMovieId(movieId!!)
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, ReviewsFragment()).commit()
            }
            binding!!.bottomBar.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.reviews -> {
                        binding!!.label.setText(R.string.MoviesReviews)
                        sharedViewModel!!.setMovieId(movieId!!)
                        supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.nav_host_fragment, ReviewsFragment()).commit()

                    }
                    R.id.recommendations -> {
                        binding!!.label.setText(R.string.MoviesRecommendations)
                        sharedViewModel!!.setMovieId(movieId!!)
                        supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.nav_host_fragment, RecommendationsFragment()).commit()

                    }
                    R.id.credits -> {
                        binding!!.label.setText(R.string.MoviesCredits)
                        sharedViewModel!!.setMovieId(movieId!!)
                        supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.nav_host_fragment, CreditsFragment()).commit()
                    }
                }
                true
            }
        }

    }
}
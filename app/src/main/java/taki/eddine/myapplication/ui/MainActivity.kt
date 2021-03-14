package taki.eddine.myapplication.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import com.jama.carouselview.CarouselViewListener
import com.jama.carouselview.enums.OffsetType
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.filmcreditslayout.*
import maes.tech.intentanim.CustomIntent.customType
import taki.eddine.myapplication.Extras
import taki.eddine.myapplication.R
import taki.eddine.myapplication.recycler.ViewPagerAdateprs.MoviesPagerAdapter
import taki.eddine.myapplication.databinding.ActivityMainBinding
import taki.eddine.myapplication.datamodels.extramodels.slider.SliderResponse
import taki.eddine.myapplication.datamodels.extramodels.slider.SliderResultItem
import taki.eddine.myapplication.mvvm.MainViewModel
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var adapter: MoviesPagerAdapter
    private val mainViewModel: MainViewModel? by viewModels()
    private var compositeDisposable: CompositeDisposable? = null
    private var activityMainBinding: ActivityMainBinding? = null
    private val binding get() = activityMainBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setApplicationLocale()
        setApplicationMode()
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        compositeDisposable = CompositeDisposable()
        Timber.d("onCreate is called..")

        activityMainBinding!!.refreshImage.setOnClickListener {
            if(Extras.isNetworkConnected(this)){
                Intent(this@MainActivity, MainActivity::class.java).apply {
                   // flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(this)
                    customType(this@MainActivity,"fadein-to-fadeout")
                }
            }
        }
        activityMainBinding!!.bestImg.setOnClickListener {
            Intent(this@MainActivity, FavDetailsActivity::class.java).apply {
                startActivity(this)
                customType(this@MainActivity,"fadein-to-fadeout")
            }
        }
        activityMainBinding!!.settingsImg.setOnClickListener {
            Intent(this@MainActivity, MovieSettingsActivity::class.java).apply {
                startActivity(this)
                customType(this@MainActivity,"fadein-to-fadeout")
            }

        }

        getLatestMovies()
        initViewPager()

    }

    private fun initViewPager() {
        adapter = MoviesPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        activityMainBinding!!.viewpager.offscreenPageLimit = 4
        activityMainBinding!!.viewpager.adapter = adapter

    }

    private fun setApplicationLocale() {
        val languagesPrefs: SharedPreferences = getSharedPreferences("languages", Context.MODE_PRIVATE)
        val language: String = languagesPrefs.getString("language", "en")!!
        val languageCode: String = languagesPrefs.getString("languageCode", "en-US")!!
        val locale = Locale(language, languageCode)
        val configuration: Configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, DisplayMetrics())
    }

    private fun setApplicationMode() {
        val modesPrefs: SharedPreferences = getSharedPreferences("darkMode", Context.MODE_PRIVATE)
        val isDarkMode: Boolean = modesPrefs.getBoolean("mode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    @SuppressLint("CheckResult")
    private fun getLatestMovies() {
        if(Extras.isNetworkConnected(this)){
            mainViewModel?.getSliderMovies(Extras.getLanguageCode(this)!!)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.retry { cause ->
                        if(!Extras.isNetworkConnected(this) || cause is Exception){
                            return@retry true
                        }
                        return@retry false
                    }
                    ?.doOnSubscribe {
                        compositeDisposable?.add(it)
                    }
                    ?.doOnNext {
                        it.results?.map { data ->
                            mainViewModel!!.insertSliderMovies(data)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(object : CompletableObserver{
                                        override fun onSubscribe(d: Disposable) {
                                        }

                                        override fun onComplete() {
                                            Timber.d("Job Completed..")
                                        }

                                        override fun onError(e: Throwable) {
                                            Timber.d("Thrower ${e.message}")
                                        }
                                    })
                        }
                    }
                    ?.subscribe({ latestMealsModel ->
                        latestMealsModel.results?.let { getSliderMovies(it) }

                    }, {
                        Timber.d("Exception Here ${it.message}")
                    })

        } else {
           mainViewModel!!.getSliderMoviesFromDB().observe(this, androidx.lifecycle.Observer {
               getSliderMovies(it)
           })
        }

    }

    private fun getSliderMovies(latestMealsModel : List<SliderResultItem>){
        activityMainBinding!!.carousel.resource = R.layout.carouselitem
        for (i in latestMealsModel.indices) {
            activityMainBinding!!.carousel.autoPlay = true
            activityMainBinding!!.carousel.carouselOffset = OffsetType.START
            activityMainBinding!!.carousel.size = latestMealsModel.size
            activityMainBinding!!.carousel.carouselViewListener = CarouselViewListener { view, position ->
                val ratingBar  : RatingBar = view.findViewById(R.id.carouselMovieRating)
                val movieTitle : TextView = view.findViewById(R.id.movieTitle)
                val imageView  : ImageView = view.findViewById(R.id.imageView)
                ratingBar.numStars = latestMealsModel[position].voteAverage?.toDouble()?.toInt()!!
                movieTitle.text = latestMealsModel[position].title
                Picasso.get().load("https://image.tmdb.org/t/p/w185//${latestMealsModel[position].posterPath}").fit().into(imageView)
                imageView.setOnClickListener {
                    if(Extras.isNetworkConnected(this)){
                        Intent(this@MainActivity, MoviesDetailsActivity::class.java).apply {
                            putExtra("movieId", latestMealsModel[position].id)
                            startActivity(this)
                            customType(this@MainActivity,"fadein-to-fadeout")
                        }
                    }
                }
            }
        }
        activityMainBinding!!.carousel.show()
    }
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.dispose()
        activityMainBinding = null
    }
}
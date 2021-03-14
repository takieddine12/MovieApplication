package taki.eddine.myapplication.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.youtube.player.*
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import maes.tech.intentanim.CustomIntent.customType
import taki.eddine.myapplication.BuildConfig
import taki.eddine.myapplication.Extras
import taki.eddine.myapplication.R
import taki.eddine.myapplication.databinding.ActivityMoviesDetails2Binding
import taki.eddine.myapplication.datamodels.DataModel
import taki.eddine.myapplication.mvvm.MainViewModel
import timber.log.Timber


@AndroidEntryPoint
class MoviesDetailsActivity : AppCompatActivity(),YouTubePlayer.OnInitializedListener {
    private var movieId = ""
    private  val ApiKey = "AIzaSyD_AHJ5nSw7lcq-z93AuCAfMwvLcRD6yus"
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var  compositeDisposable : CompositeDisposable
    private var _binding: ActivityMoviesDetails2Binding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMoviesDetails2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        compositeDisposable = CompositeDisposable()

        val frag = supportFragmentManager.findFragmentById(binding.youtubePlayer.id) as YouTubePlayerSupportFragmentX
        frag.initialize(ApiKey,this)


        val intent: Intent = intent
        intent.let {
            movieId = it.getIntExtra("movieId", 0).toString()
        }

        getDetails(movieId.toInt())
        binding.checkMore.setOnClickListener {
            Intent(this@MoviesDetailsActivity, MovieExtrasActivity::class.java).apply {
                putExtra("movieId", movieId)
                startActivity(this)
                customType(this@MoviesDetailsActivity,"fadein-to-fadeout")
            }
        }
    }

    private fun getDetails(id: Int) {
        val disposable = mainViewModel.getMoviesDetails(id, Extras.getLanguageCode(this))
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ moviesDetailsModel ->
                    binding.model = moviesDetailsModel
                    val movieRating = moviesDetailsModel?.voteAverage?.toDouble()?.toInt()!!
                    binding.movieRating.numStars = movieRating

                    val stringBuilder = StringBuilder()
                    for(genre in moviesDetailsModel.genres!!){
                        stringBuilder.append(genre.name.plus(" \u2022 "))
                    }
                    binding.movieGenre.text = stringBuilder.toString().substring(0,stringBuilder.length -2)

                    val dataModel = DataModel("https://image.tmdb.org/t/p/w185//${moviesDetailsModel.posterPath}", moviesDetailsModel.originalTitle!!, moviesDetailsModel.releaseDate!!, moviesDetailsModel.id, movieRating)
                    binding.addFav.setOnClickListener {
                        mainViewModel.insertTask(dataModel)
                                ?.subscribeOn(Schedulers.io())
                                ?.observeOn(AndroidSchedulers.mainThread())
                                ?.subscribe(object : CompletableObserver {
                                    override fun onSubscribe(d: Disposable) {}
                                    override fun onComplete() {
                                        Toast.makeText(this@MoviesDetailsActivity,getString(R.string.moviesaved),Toast.LENGTH_SHORT).show()
                                    }
                                    override fun onError(e: Throwable) {

                                    }
                                })
                    }
                    binding.openSource.setOnClickListener {
                        openSourceUrl(moviesDetailsModel.homepage!!)
                    }
                }, { t: Throwable? -> Timber.d("Movie Details ${t?.message}") })!!
        compositeDisposable.add(disposable)
    }
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        _binding = null
    }
    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, p1: YouTubePlayer?, p2: Boolean) {
        mainViewModel.getMovieTrailer(movieId, BuildConfig.MoviesApiKey, Extras.getLanguageCode(this))
                .observe(this, Observer {
                    it.results!!.map {
                        p1?.cueVideo(it.key)
                        p1?.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                    }
                })
    }
    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
      Timber.d("Youtube Player Exception ..")
    }


    private fun openSourceUrl(uri : String){
        Intent().apply {
            action = Intent.ACTION_VIEW
            data =  Uri.parse(uri)
            startActivity(Intent.createChooser(this,"Open Source"))
        }
    }


}
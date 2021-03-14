package taki.eddine.myapplication.rooom.paginglibrary

import android.annotation.SuppressLint
import android.content.Context
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import taki.eddine.myapplication.BuildConfig
import taki.eddine.myapplication.Extras
import taki.eddine.myapplication.datamodels.MovieResultItem
import taki.eddine.myapplication.network.ApiInterface
import taki.eddine.myapplication.rooom.moviesdaos.PopularMoviesDao
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

class NowPlayingBoundaryCallback(private var compositeDisposable: CompositeDisposable,
        private var popularDao: PopularMoviesDao, var context: Context, var apiInterface: ApiInterface
) : PagedList.BoundaryCallback<MovieResultItem>() {


    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        getNowPlayingMovies()
    }

    override fun onItemAtFrontLoaded(itemAtFront: MovieResultItem) {
        super.onItemAtFrontLoaded(itemAtFront)
        getNowPlayingMovies()
    }

    override fun onItemAtEndLoaded(itemAtEnd: MovieResultItem) {
        super.onItemAtEndLoaded(itemAtEnd)
        getNowPlayingMovies()
    }


    @SuppressLint("CheckResult")
    private fun getNowPlayingMovies() {

        apiInterface.getNowPlaying(BuildConfig.MoviesApiKey,
                Extras.getLanguageCode(context), getCurrentKey(context))
                ?.toObservable()
                ?.doOnSubscribe {
                    compositeDisposable.add(it)
                }
                ?.subscribeOn(Schedulers.io())
                ?.map {
                    it.results
                }
                ?.subscribe({
                    it?.map { movieResultItem ->
                        movieResultItem.uniqueIdentifier = Extras.NOWPLAY_VALue
                        popularDao.insertPopularMovies(movieResultItem)
                    }

                }, {
                    Timber.d("Now Playing Movies Exception ${it.message}")
                })
    }
}

private fun getCurrentKey(context: Context) : Int{
    val nowPlayingPrefs = context.getSharedPreferences(Extras.NOWPLAY_PREFS,Context.MODE_PRIVATE)
    return nowPlayingPrefs.getInt("value",1)
}
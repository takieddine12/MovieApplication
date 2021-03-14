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

class TopRatedBoundaryCallback(
        private val compositeDisposable: CompositeDisposable,
        private var popularDao : PopularMoviesDao
        , var context: Context
        , var apiInterface: ApiInterface
) : PagedList.BoundaryCallback<MovieResultItem>() {

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        getTopRatedMovies()
    }

    override fun onItemAtFrontLoaded(itemAtFront: MovieResultItem) {
        super.onItemAtFrontLoaded(itemAtFront)
        getTopRatedMovies()
    }

    override fun onItemAtEndLoaded(itemAtEnd: MovieResultItem) {
        super.onItemAtEndLoaded(itemAtEnd)
        getTopRatedMovies()
    }

    @SuppressLint("CheckResult")
    private fun getTopRatedMovies() {
        apiInterface.getTopRatedMovies(BuildConfig.MoviesApiKey,
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
                    it?.map { movieResult ->
                        movieResult.uniqueIdentifier = Extras.TOPRATED_VALUE
                        popularDao.insertPopularMovies(movieResult)
                    }

                },{
                   Timber.d("Top Rated Movies Exception ${it.message}")
                })
    }
}

private fun getCurrentKey(context: Context) : Int{
    val topRatedPrefs = context.getSharedPreferences(Extras.TOPRATED_PREFS,Context.MODE_PRIVATE)
    return topRatedPrefs.getInt("value",1)
}
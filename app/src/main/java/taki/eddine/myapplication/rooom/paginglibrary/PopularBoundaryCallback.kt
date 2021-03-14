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

class PopularBoundaryCallback(
        private val compositeDisposable: CompositeDisposable,
        private var popularMoviesDao: PopularMoviesDao,
        var context: Context, var apiInterface: ApiInterface) :
        PagedList.BoundaryCallback<MovieResultItem>() {

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        Timber.d("onZeroItem Called")
        getMovieGenre()
    }

    override fun onItemAtFrontLoaded(itemAtFront: MovieResultItem) {
        super.onItemAtFrontLoaded(itemAtFront)
        Timber.d("itemAtFront Called")
        getMovieGenre()
    }

    override fun onItemAtEndLoaded(itemAtEnd: MovieResultItem) {
        super.onItemAtEndLoaded(itemAtEnd)
        Timber.d("itemAtEnd Called")
        getMovieGenre()
    }


    @SuppressLint("CheckResult")
    private fun getMovieGenre() {
        apiInterface.getPopularMovies(BuildConfig.MoviesApiKey,
                Extras.getLanguageCode(context), getCurrentKey(context))
                .toObservable()
                ?.doOnSubscribe {
                    compositeDisposable.add(it)
                }
                ?.subscribeOn(Schedulers.io())
                ?.map {
                    it.results
                }
                ?.subscribe({
                    it!!.map {
                        it.uniqueIdentifier = Extras.POPULAR_VALUE
                        popularMoviesDao.insertPopularMovies(it)
                    }
                }, {
                    Timber.d("Boundary Popular Movies Exception ${it.message}")
                })
    }
}

private fun getCurrentKey(context: Context): Int {
    val popularPrefs = context.getSharedPreferences(Extras.POPULAR_PREFS, Context.MODE_PRIVATE)
    return popularPrefs.getInt("value", 1)
}

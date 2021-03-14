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

class UpcomingMoviesBoundaryCallback(
        private val compositeDisposable: CompositeDisposable,
        private var popularDao : PopularMoviesDao
        , var context: Context
        , var apiInterface: ApiInterface
) : PagedList.BoundaryCallback<MovieResultItem>() {

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        getUpcomingMovies()
    }

    override fun onItemAtFrontLoaded(itemAtFront: MovieResultItem) {
        super.onItemAtFrontLoaded(itemAtFront)
        getUpcomingMovies()
    }

    override fun onItemAtEndLoaded(itemAtEnd: MovieResultItem) {
        super.onItemAtEndLoaded(itemAtEnd)
        getUpcomingMovies()
    }

    @SuppressLint("CheckResult")
    private fun getUpcomingMovies(){
    apiInterface.getUpcomingMovies(
              BuildConfig.MoviesApiKey,Extras.getLanguageCode(context), getCurrentKey(context))
              ?.toObservable()
              ?.doOnSubscribe {
                  compositeDisposable.add(it)
              }
              ?.subscribeOn(Schedulers.io())
              ?.map {
                  it.results
              }
              ?.subscribe ({
                  it?.map { movieResult ->
                      movieResult.uniqueIdentifier = Extras.UPCOMING_VALUE
                      popularDao.insertPopularMovies(movieResult)
                  }

              },{
                  Timber.d("Boundary Upcoming Movies Exception ${it.message}")
              })
    }

}

private fun getCurrentKey(context: Context) : Int{
    val upcomingPrefs = context.getSharedPreferences(Extras.UPCOMING_PREFS,Context.MODE_PRIVATE)
    return upcomingPrefs.getInt("value",1)
}
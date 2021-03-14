package taki.eddine.myapplication.PaginationUi.pagination.upcoming

import android.content.Context
import androidx.paging.PageKeyedDataSource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import taki.eddine.myapplication.BuildConfig
import taki.eddine.myapplication.Extras
import taki.eddine.myapplication.datamodels.MovieResultItem
import taki.eddine.myapplication.network.ApiInterface
import timber.log.Timber

class UpcomingDataSource(
                     private val context: Context,
                     private val apiInterface: ApiInterface,
                     private val languageCode: String,
                     private val compositeDisposable: CompositeDisposable) :
        PageKeyedDataSource<Int, MovieResultItem>() {

    private var currentPage = 1

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int,MovieResultItem>) {
       compositeDisposable.add(apiInterface.getUpcomingMovies(BuildConfig.MoviesApiKey, languageCode,currentPage)
               ?.toObservable()
               ?.subscribeOn(Schedulers.io())
               ?.retry { cause ->
                   if(!Extras.isNetworkConnected(context) || cause is Exception){
                       return@retry true
                   }
                   return@retry false
               }
               ?.subscribe ({
                   callback.onResult(it?.results!!, null, currentPage + 1)
               },{
                   t: Throwable? ->
                   Timber.d("Error ${t?.message}")
               })!!)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResultItem>) {
        compositeDisposable.add(apiInterface
                .getUpcomingMovies(BuildConfig.MoviesApiKey, languageCode, params.key)
                ?.toObservable()
                ?.subscribeOn(Schedulers.io())
                ?.subscribe ({
                    val adjacentKey = if (params.key > 1) params.key - 1 else null
                    callback.onResult(it?.results!!, adjacentKey)
                    saveCurrentKey(context,params.key)
                },{
                    t: Throwable? ->
                    Timber.d("Error ${t?.message}")
                })!!)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResultItem>) {
       compositeDisposable.add(apiInterface
               .getUpcomingMovies(BuildConfig.MoviesApiKey, languageCode, params.key)
               ?.toObservable()
               ?.subscribeOn(Schedulers.io())
               ?.subscribe ({
                   callback.onResult(it?.results!!, params.key + 1)
                   saveCurrentKey(context, params.key)
               },{
                   t: Throwable? ->
                   Timber.d("Error ${t?.message}")
               })!!)
    }
}

private fun saveCurrentKey(context: Context,key : Int){
    val upcomingPrefs = context.getSharedPreferences(Extras.UPCOMING_PREFS,Context.MODE_PRIVATE)
    upcomingPrefs.edit().apply {
        putInt("value",key)
        apply()
    }
}
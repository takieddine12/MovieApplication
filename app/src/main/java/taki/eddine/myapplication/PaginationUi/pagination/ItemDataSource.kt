package taki.eddine.myapplication.PaginationUi.pagination

import android.content.Context
import androidx.paging.PageKeyedDataSource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import taki.eddine.myapplication.BuildConfig
import taki.eddine.myapplication.Extras
import taki.eddine.myapplication.datamodels.MovieResultItem
import taki.eddine.myapplication.datamodels.MoviesDataModel
import taki.eddine.myapplication.network.ApiInterface
import timber.log.Timber
import java.util.*
import java.util.function.Predicate

class ItemDataSource(
        private var context: Context,
        private val apiInterface: ApiInterface,
                     private val languageCode: String,
                     private val compositeDisposable: CompositeDisposable) :
        PageKeyedDataSource<Int, MovieResultItem>() {


    private var currentPage = 1


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, MovieResultItem>) {
       compositeDisposable.add(apiInterface.getPopularMovies(BuildConfig.MoviesApiKey, languageCode, currentPage)
               .toObservable()
               .subscribeOn(Schedulers.io())
               .subscribe ({
                   callback.onResult(it.results!!, null, currentPage + 1)
               },{
                   t: Throwable? ->
                   Timber.d("Error ${t?.message}")
               }))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResultItem>) {
        compositeDisposable.add(apiInterface
                .getPopularMovies(BuildConfig.MoviesApiKey, languageCode, params.key)
                .toObservable()
                ?.subscribeOn(Schedulers.io())

                ?.subscribe ({
                    val adjacentKey = if (params.key > 1) params.key - 1 else null
                    callback.onResult(it?.results!!, adjacentKey)
                },{
                    t: Throwable? ->
                    Timber.d("Error ${t?.message}")
                })!!)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResultItem>) {
       compositeDisposable.add(apiInterface
               .getPopularMovies(BuildConfig.MoviesApiKey, languageCode, params.key)
               .toObservable()
               .subscribeOn(Schedulers.io())
               .subscribe ({
                   callback.onResult(it?.results!!, params.key + 1)
                   saveCurrentKey(context, params.key)
               },{
                   t: Throwable? ->
                   Timber.d("Error ${t?.message}")
               })!!)
    }
}

private fun saveCurrentKey(context: Context,key : Int){
    val popularPrefs = context.getSharedPreferences(Extras.POPULAR_PREFS,Context.MODE_PRIVATE)
    popularPrefs.edit().apply {
        putInt("value",key)
        apply()
    }
}
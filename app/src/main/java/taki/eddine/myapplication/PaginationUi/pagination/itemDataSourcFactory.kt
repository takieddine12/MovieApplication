package taki.eddine.myapplication.PaginationUi.pagination

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import io.reactivex.disposables.CompositeDisposable
import taki.eddine.myapplication.datamodels.MovieResultItem
import taki.eddine.myapplication.datamodels.MoviesDataModel
import taki.eddine.myapplication.network.ApiInterface

class ItemDataSourceFactory(
        private var context: Context,
        private val apiInterface: ApiInterface,
        private val languageCode: String,
        private val compositeDisposable: CompositeDisposable) : DataSource.Factory<Int, MovieResultItem>() {
    private val mutableLiveData = MutableLiveData<PageKeyedDataSource<Int, MovieResultItem>>()
    override fun create(): DataSource<Int?,MovieResultItem?> {
        val itemDataSource = ItemDataSource(context,apiInterface, languageCode, compositeDisposable)
        mutableLiveData.postValue(itemDataSource)
        return itemDataSource
    }
}
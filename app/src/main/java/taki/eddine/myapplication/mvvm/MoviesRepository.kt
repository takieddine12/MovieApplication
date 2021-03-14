package taki.eddine.myapplication.mvvm

import android.content.Context
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import taki.eddine.myapplication.BuildConfig
import taki.eddine.myapplication.PaginationUi.pagination.ItemDataSourceFactory
import taki.eddine.myapplication.network.ApiInterface
import taki.eddine.myapplication.PaginationUi.pagination.nowplaying.NowPlayingDataSourcFactory
import taki.eddine.myapplication.PaginationUi.pagination.toprated.TopRatedDataSourcFactory
import taki.eddine.myapplication.PaginationUi.pagination.upcoming.UpcomingDataSourcFactory
import taki.eddine.myapplication.rooom.MoviesDao
import taki.eddine.myapplication.datamodels.DataModel
import taki.eddine.myapplication.datamodels.MovieResultItem
import taki.eddine.myapplication.datamodels.extramodels.actor.ActorResponse
import taki.eddine.myapplication.datamodels.extramodels.credits.CreditsResponse
import taki.eddine.myapplication.datamodels.extramodels.moviesdetails.DetailsResponse
import taki.eddine.myapplication.datamodels.extramodels.recommendations.RecommendationsResponse
import taki.eddine.myapplication.datamodels.extramodels.slider.SliderResponse
import taki.eddine.myapplication.datamodels.extramodels.slider.SliderResultItem
import taki.eddine.myapplication.datamodels.extramodels.videos.VideoResponse
import taki.eddine.myapplication.rooom.moviesdaos.PopularMoviesDao
import taki.eddine.myapplication.rooom.SliderMoviesDao
import taki.eddine.myapplication.rooom.paginglibrary.NowPlayingBoundaryCallback
import taki.eddine.myapplication.rooom.paginglibrary.PopularBoundaryCallback
import taki.eddine.myapplication.rooom.paginglibrary.TopRatedBoundaryCallback
import taki.eddine.myapplication.rooom.paginglibrary.UpcomingMoviesBoundaryCallback
import javax.inject.Inject

class MoviesRepository @Inject constructor(
        private val compositeDisposable: CompositeDisposable,
        private var apiInterface: ApiInterface,
                                           private var moviesDao: MoviesDao,
                                           private var sliderMoviesDao: SliderMoviesDao,
                                           private var popularDao: PopularMoviesDao,
                                           private var context: Context) {
    private val liveData: MutableLiveData<VideoResponse> = MutableLiveData()

    // TODO : Return Boundary CallBac

    fun getPopularBoundaryCallback() : PagedList.BoundaryCallback<MovieResultItem>{
        return PopularBoundaryCallback(compositeDisposable,popularDao,context,apiInterface)
    }
    fun getUpcomingBoundaryCallback() : PagedList.BoundaryCallback<MovieResultItem>{
        return UpcomingMoviesBoundaryCallback(compositeDisposable,popularDao,context,apiInterface)
    }

    fun getNowPlayingBoundaryCallback() : PagedList.BoundaryCallback<MovieResultItem>{
        return NowPlayingBoundaryCallback(compositeDisposable,popularDao,context,apiInterface)
    }
    fun getTopRatedBoundaryCallback() : PagedList.BoundaryCallback<MovieResultItem>{
        return TopRatedBoundaryCallback(compositeDisposable,popularDao,context,apiInterface)
    }

    // TODO : Data Sources Factory
    fun providePopularMovies(languageCode: String?,compositeDisposable: CompositeDisposable): ItemDataSourceFactory {
        return ItemDataSourceFactory(context,apiInterface, languageCode!!,compositeDisposable)
    }

    fun provideUpcomingMovies(languageCode: String?,compositeDisposable: CompositeDisposable): UpcomingDataSourcFactory {
        return UpcomingDataSourcFactory(context,apiInterface, languageCode!!,compositeDisposable)
    }

    fun provideTopRated(languageCode: String?,compositeDisposable: CompositeDisposable): TopRatedDataSourcFactory {
        return TopRatedDataSourcFactory(context,apiInterface, languageCode!!,compositeDisposable)
    }

    fun provideNowPlaying(languageCode: String?, compositeDisposable: CompositeDisposable): NowPlayingDataSourcFactory {
        return NowPlayingDataSourcFactory(context,apiInterface, languageCode!!,compositeDisposable)
    }


    // TODO : Api Request Operations
    fun getSliderMovies(languageCode : String) : Observable<SliderResponse>{
       return apiInterface.getSliderMovies(BuildConfig.MoviesApiKey,languageCode,1,5).toObservable()
    }

    fun getMovieDetails(id: Int, language: String?): Observable<DetailsResponse?>? {
        return apiInterface.getDetails(id, BuildConfig.MoviesApiKey, language)
                ?.toObservable()?.subscribeOn(Schedulers.io())

    }


    fun getMovieTrailer(movieId: String?, apiKey: String?, language: String?): LiveData<VideoResponse> {
        apiInterface.getMovieTrailer(movieId, apiKey, language)
                ?.enqueue(object : Callback<VideoResponse?> {
                    override fun onResponse(call: Call<VideoResponse?>, response: Response<VideoResponse?>) {
                        if (response.isSuccessful) {
                            liveData.postValue(response.body())
                        }
                    }

                    override fun onFailure(call: Call<VideoResponse?>, t: Throwable) {}
                })
        return liveData
    }

    fun getMovieExtras(movieId: String?, apiKey: String?, language: String?, page: Int): Observable<taki.eddine.myapplication.datamodels.extramodels.ReviewsResponse?>? {
        return apiInterface.getMovieExtras(movieId, apiKey, language, page)?.toObservable()
    }

    fun getMovieRecommendations(movieId: String?, apiKey: String?, language: String?, page: Int): Observable<RecommendationsResponse?>? {
        return apiInterface.getMovieRecommendations(movieId, apiKey, language, page)?.toObservable()
    }

    fun getMovieCredits(movieId: String?, apiKey: String?, language: String?): Observable<CreditsResponse?>? {
        return apiInterface.getMovieCredits(movieId, apiKey, language)?.toObservable()
    }

    fun getActorInfo(personId : Int , apiKey : String , language: String) : Observable<ActorResponse>{
        return apiInterface.getActorInfo(personId,apiKey,language).toObservable()
    }

    // TODO : Dao Operations Here
    fun getAllSavedMovies() : LiveData<MutableList<DataModel>>{
        return moviesDao.getSavedMovies()
    }

    fun insertTask(dataModel: DataModel?): Completable {
        return moviesDao.insertMovies(dataModel)!!
    }

    fun deletePerMovie(dataModel: DataModel?): Completable {
        return moviesDao.deleteMovie(dataModel)!!
    }

    fun deleteAllMovies(): Completable {
        return moviesDao.deleteAllMovies()!!
    }

    fun deleteDuplicateData() : Completable?{
        return moviesDao.deleteDuplicate()
    }

    // TODO: Slider Movies Dao
    fun insertSliderMovies(model : SliderResultItem) : Completable {
      return  sliderMoviesDao.insertSliderMovies(model)
    }
    fun getSliderMoviesFromDB() : LiveData<List<SliderResultItem>>{
        return sliderMoviesDao.getSliderMovies()
    }


    // Paging Library Operations
    fun getPopularMoviesFromDB(identifier : String) : DataSource.Factory<Int, MovieResultItem>{
        return popularDao.getPopularMoviesList(identifier)
    }


}
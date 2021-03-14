package taki.eddine.myapplication.mvvm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import kotlinx.coroutines.flow.MutableStateFlow
import taki.eddine.myapplication.datamodels.DataModel
import taki.eddine.myapplication.datamodels.MovieResultItem
import taki.eddine.myapplication.datamodels.extramodels.ReviewsResponse
import taki.eddine.myapplication.datamodels.extramodels.actor.ActorResponse
import taki.eddine.myapplication.datamodels.extramodels.credits.CreditsResponse
import taki.eddine.myapplication.datamodels.extramodels.moviesdetails.DetailsResponse
import taki.eddine.myapplication.datamodels.extramodels.recommendations.RecommendationsResponse
import taki.eddine.myapplication.datamodels.extramodels.slider.SliderResponse
import taki.eddine.myapplication.datamodels.extramodels.slider.SliderResultItem
import taki.eddine.myapplication.datamodels.extramodels.videos.VideoResponse
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
        private var compositeDisposable: CompositeDisposable,
        private var repository: MoviesRepository) : ViewModel() {

    //---------------------------------------------
    private val _stateFlow : MutableStateFlow<UiStates> = MutableStateFlow(UiStates.EMPTY)
    // TODO : Get Popular
    fun getPopularMoviesInfo(languageCode: String?): MutableStateFlow<UiStates> {
        _stateFlow.value = UiStates.LOADING
        try {
            val data = LivePagedListBuilder(
                    repository.providePopularMovies(languageCode, compositeDisposable), getBuilderConfig())
                    .setBoundaryCallback(repository.getPopularBoundaryCallback()).build()
            _stateFlow.value = UiStates.SUCCESS(data)

        }catch (ex: Exception){
            _stateFlow.value = UiStates.ERROR(ex.message!!)
        }
       return _stateFlow
    }
    // TODO : Get TopRated
    fun getTopRatedMoviesInfo(languageCode: String?): MutableStateFlow<UiStates>{
        _stateFlow.value = UiStates.LOADING
        try {
            val data =  LivePagedListBuilder(
                    repository.provideTopRated(languageCode, compositeDisposable), getBuilderConfig())
                    .setBoundaryCallback(repository.getTopRatedBoundaryCallback()).build()
            _stateFlow.value = UiStates.SUCCESS(data)
        }catch (ex: Exception){
            _stateFlow.value = UiStates.ERROR(ex.message!!)
        }
        return _stateFlow
    }
    // TODO : Get Upcoming
    fun getUpcomingMoviesInfo(languageCode: String?): MutableStateFlow<UiStates> {
        _stateFlow.value = UiStates.LOADING
        try {

            val data =  LivePagedListBuilder(
                    repository.provideUpcomingMovies(languageCode, compositeDisposable), getBuilderConfig())
                    .setBoundaryCallback(repository.getUpcomingBoundaryCallback()).build()

            _stateFlow.value = UiStates.SUCCESS(data)
        }catch (ex: Exception){
            _stateFlow.value = UiStates.ERROR(ex.message!!)

        }
        return _stateFlow
    }
    // TODO : Get NowPlaying
    fun getNowPlayingMoviesInfo(languageCode: String?): MutableStateFlow<UiStates>{
        _stateFlow.value = UiStates.LOADING
        try {

            val data =  LivePagedListBuilder(
                    repository.provideNowPlaying(languageCode, compositeDisposable), getBuilderConfig())
                    .setBoundaryCallback(repository.getNowPlayingBoundaryCallback()).build()
            _stateFlow.value = UiStates.SUCCESS(data)
        }catch (ex: Exception){
            _stateFlow.value = UiStates.ERROR(ex.message!!)

        }
      return _stateFlow
    }

   // ------------------------------------------------------------------
    fun getSliderMovies(languageCode: String) : Observable<SliderResponse>{
        return repository.getSliderMovies(languageCode)
    }

    fun getMovieExtras(movieId: String?, apiKey: String?, language: String?, page: Int): Observable<ReviewsResponse?>? {
        return repository.getMovieExtras(movieId, apiKey, language, page)
    }

    fun getMovieRecommendations(movieId: String?, apiKey: String?, language: String?, page: Int): Observable<RecommendationsResponse?>? {
        return repository.getMovieRecommendations(movieId, apiKey, language, page)
    }

    fun getMovieCredits(movieId: String?, apiKey: String?, language: String?): Observable<CreditsResponse?>? {
        return repository.getMovieCredits(movieId, apiKey, language)
    }

    fun getMovieTrailer(movieId: String?, apiKey: String?, language: String?): LiveData<VideoResponse> {
        return repository.getMovieTrailer(movieId, apiKey, language)
    }

     //---------------------------------------------------------------------------------------------

    fun getMoviesDetails(id: Int, language: String?): Observable<DetailsResponse?>? {
        return repository.getMovieDetails(id, language)
    }

    fun getActorInfo(personId: Int, apiKey: String, language: String) : Observable<ActorResponse> {
        return repository.getActorInfo(personId, apiKey, language)
    }
    //----------------------------------------
    fun getAllSavedMovies() : LiveData<MutableList<DataModel>> {
      return  repository.getAllSavedMovies()
    }


    fun insertTask(dataModel: DataModel?): Completable? {
        return repository.insertTask(dataModel)
    }

    fun deletePerMovie(dataModel: DataModel?): Completable? {
        return repository.deletePerMovie(dataModel)
    }

    fun deleteAllMovies(): Completable? {
        return repository.deleteAllMovies()
    }

    fun deleteDuplicateData() : Completable?{
        return repository.deleteDuplicateData()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    sealed class UiStates {
        object LOADING : UiStates()
        data class SUCCESS(var data: LiveData<PagedList<MovieResultItem?>>) : UiStates()
        data class ERROR(var exception: String) : UiStates()
        object EMPTY : UiStates()
    }


    // TODO : LiveDataBuilder config
    private fun getBuilderConfig() : PagedList.Config {
        return  PagedList.Config.Builder()
                .setPageSize(20)
                .setPrefetchDistance(1)
                .setInitialLoadSizeHint(20)
                .setEnablePlaceholders(true)
                .build()
    }


    fun getPopularMoviesFromDB(identifier: String) : LiveData<PagedList<MovieResultItem>> {
       return LivePagedListBuilder(repository.getPopularMoviesFromDB(identifier), getBuilderConfig()).build()
    }

    // TODO : Slider Dao Operations
    fun getSliderMoviesFromDB() : LiveData<List<SliderResultItem>> {
        return repository.getSliderMoviesFromDB()
    }

    fun insertSliderMovies(model: SliderResultItem) : Completable {
        return repository.insertSliderMovies(model)
    }
}


/*
 private fun getBuilderConfig() : PagedList.Config {
        return  PagedList.Config.Builder()
                .setPageSize(20)
                .setPrefetchDistance(1)
                .setInitialLoadSizeHint(6)
                .build()
    }
 */


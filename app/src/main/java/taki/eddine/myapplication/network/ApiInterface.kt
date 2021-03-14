package taki.eddine.myapplication.network

import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import taki.eddine.myapplication.datamodels.extramodels.ReviewsResponse
import taki.eddine.myapplication.datamodels.extramodels.actor.ActorResponse
import taki.eddine.myapplication.datamodels.extramodels.credits.CreditsResponse
import taki.eddine.myapplication.datamodels.extramodels.moviesdetails.DetailsResponse
import taki.eddine.myapplication.datamodels.extramodels.recommendations.RecommendationsResponse
import taki.eddine.myapplication.datamodels.extramodels.slider.SliderResponse
import taki.eddine.myapplication.datamodels.extramodels.videos.VideoResponse
import taki.eddine.myapplication.datamodels.LatestMoviesModel
import taki.eddine.myapplication.datamodels.MoviesDataModel

interface ApiInterface {

    @GET("movie/popular?")
    fun getPopularMovies(
            @Query("api_key") ApiKey: String?,
            @Query("language") language: String?,
            @Query("page") page: Int): Flowable<MoviesDataModel>

     @GET("movie/upcoming?")
     fun getUpcomingMovies(
             @Query("api_key") ApiKey: String?,
             @Query("language") language: String?,
             @Query("page") page: Int): Flowable<MoviesDataModel?>?

     @GET("movie/top_rated?")
     fun getTopRatedMovies(
             @Query("api_key") ApiKey: String?,
             @Query("language") language: String?,
             @Query("page") page: Int): Flowable<MoviesDataModel?>?

     @GET("movie/now_playing?")
     fun getNowPlaying(
             @Query("api_key") ApiKey: String?,
             @Query("language") language: String?,
             @Query("page") page: Int): Flowable<MoviesDataModel?>?


     @GET("movie/{id}")
    fun getDetails(@Path("id") movieId: Int,
                   @Query("api_key") ApiKey: String?,
                   @Query("language") language: String?): Flowable<DetailsResponse?>?

    @GET("search/movie?")
    fun getSearchMovie(@Query("api_key") ApiKey: String?,
                       @Query("language") language: String?,
                       @Query("query") query: String?,
                       @Query("page") page: Int,
                       @Query("include_adult") adult: Boolean): Call<MoviesDataModel?>?

    @GET("movie/latest?")
    fun getLatestMeals(@Query("api_key") ApiKey: String?): Flowable<LatestMoviesModel?>?

    @GET("movie/{id}/reviews?")
    fun getMovieExtras(
            @Path("id") movieId: String?,
            @Query("api_key") ApiKey: String?,
            @Query("language") language: String?,
            @Query("page") page: Int
    ): Flowable<ReviewsResponse?>?

    @GET("movie/{id}/recommendations?")
    fun getMovieRecommendations(
            @Path("id") movieId: String?,
            @Query("api_key") ApiKey: String?,
            @Query("language") language: String?,
            @Query("page") page: Int
    ): Flowable<RecommendationsResponse?>?

    @GET("movie/{id}/credits?")
    fun getMovieCredits(
            @Path("id") movieId: String?,
            @Query("api_key") ApiKey: String?,
            @Query("language") language: String?
    ): Flowable<CreditsResponse?>?

    @GET("movie/{id}/videos?")
    fun getMovieTrailer(
            @Path("id") movieId: String?,
            @Query("api_key") ApiKey: String?,
            @Query("language") language: String?
    ): Call<VideoResponse?>?


    @GET("movie/top_rated?")
    fun getSliderMovies(
            @Query("api_key") ApiKey: String?,
            @Query("language") language: String?,
            @Query("page") page: Int,
            @Query("pageSize") pageSize: Int):  Flowable<SliderResponse>

    @GET("person/{person_id}")
    fun getActorInfo(
            @Path("person_id") personId : Int?,
            @Query("api_key") ApiKey: String?,
            @Query("language") language: String?,
    ) : Flowable<ActorResponse>
}
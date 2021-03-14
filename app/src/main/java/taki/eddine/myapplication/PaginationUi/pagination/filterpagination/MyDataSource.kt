package taki.eddine.myapplication.PaginationUi.pagination.filterpagination

//import androidx.paging.PageKeyedDataSource
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import taki.eddine.myapplication.BuildConfig
//import taki.eddine.myapplication.network.ApiInterface
//import java.util.concurrent.Executor
//
//class MyDataSource(private val input: String, private val executor: Executor) : PageKeyedDataSource<Int, MoviesDataModel?>() {
//    var page = 1
//    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, MoviesDataModel?>) {
//        val httpLoggingInterceptor = HttpLoggingInterceptor()
//        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//        val ok = OkHttpClient.Builder()
//        ok.addInterceptor(httpLoggingInterceptor)
//        Retrofit.Builder().baseUrl(BuildConfig.PopularMovieUrl)
//                .client(ok.build())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build().create(ApiInterface::class.java)
//                .getSearchMovie(BuildConfig.MoviesApiKey, "en-US", input, page, false)
//                ?.enqueue(object : Callback<MoviesDataModel?> {
//                    override fun onResponse(call: Call<MoviesDataModel?>, response: Response<MoviesDataModel?>) {
//                        if (response.body() != null) {
//                            callback.onResult(response.body()!!.list!!, null, page)
//                        }
//                    }
//
//                    override fun onFailure(call: Call<MoviesDataModel?>, t: Throwable) {}
//                })
//    }
//
//    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MoviesDataModel?>) {
//        Retrofit.Builder().baseUrl(BuildConfig.PopularMovieUrl).addConverterFactory(GsonConverterFactory.create())
//                .build().create(ApiInterface::class.java).getSearchMovie(BuildConfig.MoviesApiKey, "en-US", input, page, false)
//                ?.enqueue(object : Callback<MoviesDataModel?> {
//                    override fun onResponse(call: Call<MoviesDataModel?>, response: Response<MoviesDataModel?>) {
//                        val adjacentKey = if (params.key < 1) params.key - 1 else null
//                        if (response.body() != null) {
//                            callback.onResult(response.body()!!.list!!, adjacentKey)
//                        }
//                    }
//
//                    override fun onFailure(call: Call<MoviesDataModel?>, t: Throwable) {}
//                })
//    }
//
//    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MoviesDataModel?>) {
//        Retrofit.Builder().baseUrl(BuildConfig.PopularMovieUrl).addConverterFactory(GsonConverterFactory.create())
//                .build().create(ApiInterface::class.java).getSearchMovie(BuildConfig.MoviesApiKey, "en-US", input, page, false)
//                ?.enqueue(object : Callback<MoviesDataModel?> {
//                    override fun onResponse(call: Call<MoviesDataModel?>, response: Response<MoviesDataModel?>) {
//                        if (response.body() != null) {
//                            callback.onResult(response.body()!!.list!!, page)
//                        }
//                    }
//
//                    override fun onFailure(call: Call<MoviesDataModel?>, t: Throwable) {}
//                })
//    }
//}
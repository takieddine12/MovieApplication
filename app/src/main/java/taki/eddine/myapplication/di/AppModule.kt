package taki.eddine.myapplication.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.DefineComponent
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import taki.eddine.myapplication.BuildConfig
import taki.eddine.myapplication.datamodels.DataModel
import taki.eddine.myapplication.datamodels.MovieResultItem
import taki.eddine.myapplication.datamodels.extramodels.slider.SliderResultItem
import taki.eddine.myapplication.network.ApiInterface
import taki.eddine.myapplication.rooom.moviesdaos.PopularMoviesDao
import taki.eddine.myapplication.rooom.MoviesDao
import taki.eddine.myapplication.rooom.MoviesDatabase
import taki.eddine.myapplication.rooom.SliderMoviesDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideRetrofitInstance(): ApiInterface {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.PopularMovieUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(ApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MoviesDatabase {
        return Room.databaseBuilder(context.applicationContext,
                MoviesDatabase::class.java, "movie.db")
                .fallbackToDestructiveMigration()
                .build() }

    @Singleton
    @Provides
    fun provideCompositeDisposable() : CompositeDisposable {
        return CompositeDisposable()
    }


     // TODO : Providing Daos
     @Singleton
    @Provides
    fun provideMovieDao(moviesDatabase: MoviesDatabase): MoviesDao {
        return moviesDatabase.moviesDao()
    }

    @Singleton
    @Provides
    fun providePopularMoviesDao(moviesDatabase: MoviesDatabase) : PopularMoviesDao {
        return moviesDatabase.popularDao()
    }

    @Singleton
    @Provides
    fun provideSliderMoviesDao(moviesDatabase: MoviesDatabase) : SliderMoviesDao {
        return moviesDatabase.latestMoviesDao()
    }

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
}

private fun getMovieResultItem() : MovieResultItem {
    return MovieResultItem(
            "Overview","Language","Title",
            false, "title","poster","dropPath",
            "releaseDate",0.0,"voteAverage",
            0,false,"voteCount"
    )
}
private fun getDataModel() : DataModel {
    return DataModel(".","Title","release Date", 0,0)
}
private fun getSliderResultItem() : SliderResultItem {
    return SliderResultItem(
            "Overview","Language","Title",
            false,"Title","poster",
            "backDrop","release Date",
            0.0,"0.0",0,false,0
    )
}

/*
@Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MoviesDatabase {
        return Room.databaseBuilder(context.applicationContext,
                MoviesDatabase::class.java, "movie.db")
                .addCallback(object : RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

//                        providePopularMoviesDao().insertPopularMovies(getMovieResultItem())
//                        provideSliderMoviesDao(context).insertSliderMovies(getSliderResultItem())
//                        provideMovieDao(context).insertMovies(getDataModel())
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
    }
 */
package taki.eddine.myapplication.rooom.moviesdaos

import androidx.room.*
import taki.eddine.myapplication.datamodels.MovieResultItem

@Dao
interface NowPlayingMoviesDao {


    @Query("SELECT * FROM MovieGenreTable Where uniqueIdentifier = :identifier order by movieGenreId")
    fun getNowPlayingMoviesList(identifier : String) : androidx.paging.DataSource.Factory<Int, MovieResultItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNowPlayingMovies(model : MovieResultItem)



}
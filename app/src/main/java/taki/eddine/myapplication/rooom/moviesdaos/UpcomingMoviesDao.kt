package taki.eddine.myapplication.rooom.moviesdaos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import taki.eddine.myapplication.datamodels.MovieResultItem
import taki.eddine.myapplication.datamodels.MoviesDataModel

@Dao
interface UpcomingMoviesDao {

    @Query("SELECT * FROM MovieGenreTable Where uniqueIdentifier = :identifier order by movieGenreId")
    fun getUpcomingMoviesList(identifier : String) : androidx.paging.DataSource.Factory<Int, MovieResultItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUpcomingMovies(model : MovieResultItem)

}
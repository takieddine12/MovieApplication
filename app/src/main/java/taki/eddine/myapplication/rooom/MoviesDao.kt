package taki.eddine.myapplication.rooom

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Completable
import taki.eddine.myapplication.datamodels.DataModel

@Dao
interface MoviesDao {
    @Query("SELECT * FROM movieTable ORDER by userMovieId")
    fun getSavedMovies() : LiveData<MutableList<DataModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(model: DataModel?): Completable?

    @Delete
    fun deleteMovie(model: DataModel?): Completable?

    @Query("DELETE FROM movieTable")
    fun deleteAllMovies(): Completable?

    ///Clear duplicates
    @Query("DELETE FROM movieTable WHERE userMovieId NOT IN (SELECT(userMovieId) FROM movieTable GROUP BY title)")
    fun deleteDuplicate() : Completable?
}
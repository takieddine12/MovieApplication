package taki.eddine.myapplication.rooom

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import taki.eddine.myapplication.datamodels.LatestMoviesModel
import taki.eddine.myapplication.datamodels.extramodels.slider.SliderResponse
import taki.eddine.myapplication.datamodels.extramodels.slider.SliderResultItem

@Dao
interface SliderMoviesDao {


    @Query("SELECT * FROM LatestMoviesTable ")
    fun getSliderMovies() : LiveData<List<SliderResultItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun  insertSliderMovies(model : SliderResultItem) : Completable
}
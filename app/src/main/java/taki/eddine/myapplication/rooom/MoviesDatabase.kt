package taki.eddine.myapplication.rooom

import androidx.room.Database
import androidx.room.RoomDatabase
import taki.eddine.myapplication.datamodels.DataModel
import taki.eddine.myapplication.datamodels.MovieResultItem
import taki.eddine.myapplication.datamodels.extramodels.slider.SliderResultItem
import taki.eddine.myapplication.rooom.moviesdaos.PopularMoviesDao


@Database(entities = [DataModel::class,MovieResultItem::class,SliderResultItem::class], version = 1, exportSchema = false)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
    abstract fun popularDao() : PopularMoviesDao
    abstract fun latestMoviesDao() : SliderMoviesDao
}
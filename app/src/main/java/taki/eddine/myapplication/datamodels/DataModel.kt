package taki.eddine.myapplication.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "movieTable")
class DataModel(var url: String = "",
                var title: String = "",
                var releaseDate: String = "",
                var movieId: Int = 0,
                var voteAverage: Int = 0) {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var userMovieId : Int? = null

}
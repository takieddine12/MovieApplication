package taki.eddine.myapplication.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity(tableName = "MovieGenreTable")
data class MovieResultItem(
        @SerializedName("overview")
        var overview: String? = null,

        @SerializedName("original_language")
        var originalLanguage: String? = null,

        @SerializedName("original_title")
        var originalTitle: String? = null,

        @SerializedName("video")
        var isVideo: Boolean? = false,

        @SerializedName("title")
        var title: String? = null,

        @SerializedName("poster_path")
        var posterPath: String? = null,

        @SerializedName("backdrop_path")
        var backdropPath: String? = null,

        @SerializedName("release_date")
        var releaseDate: String? = null,

        @SerializedName("popularity")
        var popularity: Double? = 0.0,

        @SerializedName("vote_average")
        var voteAverage: String? = null,

        @SerializedName("id")
        var id: Int? = 0,

        @SerializedName("adult")
        var isAdult: Boolean? = false,

        @SerializedName("vote_count")
        var voteCount: String? = null,
){
    var uniqueIdentifier : String? = null

    @PrimaryKey(autoGenerate = true)
    @NotNull
    var movieGenreId: Int? = null
}


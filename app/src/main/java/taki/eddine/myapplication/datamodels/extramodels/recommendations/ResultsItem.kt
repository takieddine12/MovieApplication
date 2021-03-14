package taki.eddine.myapplication.datamodels.extramodels.recommendations

import com.google.gson.annotations.SerializedName

class ResultsItem {
    @SerializedName("overview")
    var overview: String? = null

    @SerializedName("original_language")
    var originalLanguage: String? = null

    @SerializedName("original_title")
    var originalTitle: String? = null

    @SerializedName("video")
    var isVideo = false

    @SerializedName("title")
    var title: String? = null

    @SerializedName("genre_ids")
    var genreIds: List<Int>? = null

    @SerializedName("poster_path")
    var posterPath: String? = null

    @SerializedName("backdrop_path")
    var backdropPath: String? = null

    @SerializedName("release_date")
    var releaseDate: String? = null

    @SerializedName("vote_average")
    var voteAverage: String? = null

    @SerializedName("popularity")
    var popularity: String? = null

    @SerializedName("id")
    var id = 0

    @SerializedName("adult")
    var isAdult = false

    @SerializedName("vote_count")
    var voteCount: String? = null
}
package taki.eddine.myapplication.datamodels.extramodels.moviesdetails

import com.google.gson.annotations.SerializedName

class DetailsResponse {
    @SerializedName("original_language")
    var originalLanguage: String? = null

    @SerializedName("imdb_id")
    var imdbId: String? = null

    @SerializedName("video")
    var isVideo = false

    @SerializedName("title")
    var title: String? = null

    @SerializedName("backdrop_path")
    var backdropPath: String? = null

    @SerializedName("revenue")
    var revenue : String? = null

    @SerializedName("genres")
    var genres: List<GenresItem>? = null

    @SerializedName("popularity")
    var popularity = 0.0

    @SerializedName("production_countries")
    var productionCountries: List<ProductionCountriesItem>? = null

    @SerializedName("id")
    var id = 0

    @SerializedName("vote_count")
    var voteCount = 0

    @SerializedName("budget")
    var budget : String? = null

    @SerializedName("overview")
    var overview: String? = null

    @SerializedName("original_title")
    var originalTitle: String? = null

    @SerializedName("runtime")
    var runtime : String? = null

    @SerializedName("poster_path")
    var posterPath: String? = null

    @SerializedName("spoken_languages")
    var spokenLanguages: List<SpokenLanguagesItem>? = null

    @SerializedName("production_companies")
    var productionCompanies: List<ProductionCompaniesItem>? = null

    @SerializedName("release_date")
    var releaseDate: String? = null

    @SerializedName("vote_average")
    var voteAverage : String?= null

    @SerializedName("belongs_to_collection")
    var belongsToCollection: BelongsToCollection? = null

    @SerializedName("tagline")
    var tagline: String? = null

    @SerializedName("adult")
    var isAdult = false

    @SerializedName("homepage")
    var homepage: String? = null

    @SerializedName("status")
    var status: String? = null
}
package taki.eddine.myapplication.datamodels

import com.google.gson.annotations.SerializedName

class MoviesDataModel {
    @SerializedName("page")
    var page = 0

    @SerializedName("total_pages")
    var totalPages = 0

    @SerializedName("results")
    var results: List<MovieResultItem>? = null

    @SerializedName("total_results")
    var totalResults = 0
}
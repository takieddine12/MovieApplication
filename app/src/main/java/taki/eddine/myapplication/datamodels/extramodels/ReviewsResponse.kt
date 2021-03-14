package taki.eddine.myapplication.datamodels.extramodels

import com.google.gson.annotations.SerializedName

class ReviewsResponse {
    @SerializedName("id")
    val id = 0

    @SerializedName("page")
    val page = 0

    @SerializedName("total_pages")
    val totalPages = 0

    @SerializedName("results")
    val results: List<ResultsItem>? = null

    @SerializedName("total_results")
    val totalResults = 0
}
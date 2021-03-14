package taki.eddine.myapplication.datamodels.extramodels.recommendations

import com.google.gson.annotations.SerializedName

class RecommendationsResponse {
    @SerializedName("page")
    val page = 0

    @SerializedName("total_pages")
    val totalPages = 0

    @SerializedName("results")
    val results: List<ResultsItem>? = null

    @SerializedName("total_results")
    val totalResults = 0
}
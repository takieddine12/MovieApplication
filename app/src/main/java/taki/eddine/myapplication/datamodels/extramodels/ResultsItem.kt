package taki.eddine.myapplication.datamodels.extramodels

import com.google.gson.annotations.SerializedName

class ResultsItem {
    @SerializedName("author_details")
    val authorDetails: AuthorDetails? = null

    @SerializedName("updated_at")
    val updatedAt: String? = null

    @SerializedName("author")
    val author: String? = null

    @SerializedName("created_at")
    val createdAt: String? = null

    @SerializedName("id")
    val id: String? = null

    @SerializedName("content")
    val content: String? = null

    @SerializedName("url")
    val url: String? = null
}
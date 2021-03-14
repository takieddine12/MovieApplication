package taki.eddine.myapplication.datamodels.extramodels.videos

import com.google.gson.annotations.SerializedName

class VideoResponse {
    @SerializedName("id")
    var id = 0

    @SerializedName("results")
    var results: List<ResultsItem>? = null
}
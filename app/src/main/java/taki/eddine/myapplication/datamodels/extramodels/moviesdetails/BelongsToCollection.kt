package taki.eddine.myapplication.datamodels.extramodels.moviesdetails

import com.google.gson.annotations.SerializedName

class BelongsToCollection {
    @SerializedName("backdrop_path")
    var backdropPath: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("id")
    var id = 0

    @SerializedName("poster_path")
    var posterPath: String? = null
}
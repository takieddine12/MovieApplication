package taki.eddine.myapplication.datamodels.extramodels.moviesdetails

import com.google.gson.annotations.SerializedName

class GenresItem {
    @SerializedName("name")
    var name: String? = null

    @SerializedName("id")
    var id = 0
}
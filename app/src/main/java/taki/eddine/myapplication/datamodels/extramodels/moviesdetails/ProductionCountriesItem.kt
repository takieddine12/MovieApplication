package taki.eddine.myapplication.datamodels.extramodels.moviesdetails

import com.google.gson.annotations.SerializedName

class ProductionCountriesItem {
    @SerializedName("iso_3166_1")
    var iso31661: String? = null

    @SerializedName("name")
    var name: String? = null
}
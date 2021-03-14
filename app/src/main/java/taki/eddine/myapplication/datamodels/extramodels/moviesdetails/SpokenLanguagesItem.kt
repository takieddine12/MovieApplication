package taki.eddine.myapplication.datamodels.extramodels.moviesdetails

import com.google.gson.annotations.SerializedName

class SpokenLanguagesItem {
    @SerializedName("name")
    var name: String? = null

    @SerializedName("iso_639_1")
    var iso6391: String? = null

    @SerializedName("english_name")
    var englishName: String? = null
}
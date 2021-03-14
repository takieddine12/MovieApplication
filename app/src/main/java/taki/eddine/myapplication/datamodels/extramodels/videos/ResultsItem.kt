package taki.eddine.myapplication.datamodels.extramodels.videos

import com.google.gson.annotations.SerializedName

class ResultsItem {
    @SerializedName("site")
    var site: String? = null

    @SerializedName("size")
    var size = 0

    @SerializedName("iso_3166_1")
    var iso31661: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("iso_639_1")
    var iso6391: String? = null

    @SerializedName("key")
    var key: String? = null
}
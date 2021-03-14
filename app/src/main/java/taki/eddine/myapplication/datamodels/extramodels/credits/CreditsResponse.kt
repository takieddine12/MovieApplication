package taki.eddine.myapplication.datamodels.extramodels.credits

import com.google.gson.annotations.SerializedName

class CreditsResponse {
    @SerializedName("cast")
    var cast: List<CastItem>? = null

    @SerializedName("id")
    var id = 0

    @SerializedName("crew")
    var crew: List<CrewItem>? = null
}
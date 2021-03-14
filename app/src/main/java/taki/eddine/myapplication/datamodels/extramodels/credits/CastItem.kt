package taki.eddine.myapplication.datamodels.extramodels.credits

import com.google.gson.annotations.SerializedName

class CastItem {
    @SerializedName("cast_id")
    var castId = 0

    @SerializedName("character")
    var character: String? = null

    @SerializedName("gender")
    var gender = 0

    @SerializedName("credit_id")
    var creditId: String? = null

    @SerializedName("known_for_department")
    var knownForDepartment: String? = null

    @SerializedName("original_name")
    var originalName: String? = null

    @SerializedName("popularity")
    var popularity = 0.0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("profile_path")
    var profilePath: String? = null

    @SerializedName("id")
    var id = 0

    @SerializedName("adult")
    var isAdult = false

    @SerializedName("order")
    var order = 0
}
package taki.eddine.myapplication.datamodels.extramodels.credits

import com.google.gson.annotations.SerializedName

class CrewItem {
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
    var profilePath: Any? = null

    @SerializedName("id")
    var id = 0

    @SerializedName("adult")
    var isAdult = false

    @SerializedName("department")
    var department: String? = null

    @SerializedName("job")
    var job: String? = null
}
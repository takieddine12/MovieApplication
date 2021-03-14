package taki.eddine.myapplication.datamodels.extramodels.actor

import com.google.gson.annotations.SerializedName

class ActorResponse {
    @SerializedName("also_known_as")
    var alsoKnownAs: List<String>? = null

    @SerializedName("birthday")
    var birthday: String? = null

    @SerializedName("gender")
    var gender = 0

    @SerializedName("imdb_id")
    var imdbId: String? = null

    @SerializedName("known_for_department")
    var knownForDepartment: String? = null

    @SerializedName("profile_path")
    var profilePath: String? = null

    @SerializedName("biography")
    var biography: String? = null

    @SerializedName("deathday")
    var deathday: String? = null

    @SerializedName("place_of_birth")
    var placeOfBirth: String? = null

    @SerializedName("popularity")
    var popularity = 0.0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("id")
    var id = 0

    @SerializedName("adult")
    var isAdult = false

    @SerializedName("homepage")
    var homepage: Any? = null
}
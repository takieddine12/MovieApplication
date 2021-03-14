package taki.eddine.myapplication.datamodels.extramodels

import com.google.gson.annotations.SerializedName

data class AuthorDetails(
        @SerializedName("avatar_path")
        val avatarPath: String? = null,

        @SerializedName("name")
        val name: String? = null,

        @SerializedName("rating")
        val rating: Double = 0.0,

        @SerializedName("username")
        val username: String? = null,
)
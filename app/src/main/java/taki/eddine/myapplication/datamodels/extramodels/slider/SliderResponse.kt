package taki.eddine.myapplication.datamodels.extramodels.slider

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull


class SliderResponse {
    @SerializedName("page")
    var page = 0

    @SerializedName("total_pages")
    var totalPages = 0


    @SerializedName("results")
    var results: List<SliderResultItem>? = null

    @SerializedName("total_results")
    var totalResults = 0


}
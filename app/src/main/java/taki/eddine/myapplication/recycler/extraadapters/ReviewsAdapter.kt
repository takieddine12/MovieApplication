package taki.eddine.myapplication.recycler.extraadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import taki.eddine.myapplication.R
import taki.eddine.myapplication.recycler.extraadapters.ReviewsAdapter.ReviewsHolder
import taki.eddine.myapplication.databinding.FilmreviewslayoutBinding
import taki.eddine.myapplication.datamodels.extramodels.ReviewsResponse
import taki.eddine.myapplication.movieslisteners.ReadMoreListener
import timber.log.Timber

class ReviewsAdapter(private val response: ReviewsResponse,
                     private var readMoreListener: ReadMoreListener) : RecyclerView.Adapter<ReviewsHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsHolder {
        val binding: FilmreviewslayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.filmreviewslayout, parent, false)
        return ReviewsHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewsHolder, position: Int) {
        holder.binding.model1 = response.results!![position].authorDetails
        holder.binding.model2 = response.results[position]
        val rating = response.results[position].authorDetails?.rating?.toInt()
        if(rating == null){
            holder.binding.movieRating.numStars = 0
        } else {
            holder.binding.movieRating.numStars = rating
        }
        holder.binding.readMoreListener = readMoreListener
    }

    override fun getItemCount(): Int {
        return response.results!!.size
    }

    class ReviewsHolder(val binding: FilmreviewslayoutBinding) : RecyclerView.ViewHolder(binding.root)
}
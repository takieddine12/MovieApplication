package taki.eddine.myapplication.recycler.extraadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import taki.eddine.myapplication.R
import taki.eddine.myapplication.databinding.FilmrecommendationslayoutBinding
import taki.eddine.myapplication.datamodels.extramodels.recommendations.RecommendationsResponse
import taki.eddine.myapplication.movieslisteners.RecommendationsMovieListener

class RecommendationsAdapter(private val recommendationsResponse: RecommendationsResponse,
            private val recommendationsMovieListener: RecommendationsMovieListener) : RecyclerView.Adapter<RecommendationsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: FilmrecommendationslayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.filmrecommendationslayout, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = recommendationsResponse.results!![position]
        val voteAverage = recommendationsResponse.results[position].voteAverage!!.toDouble().toInt()
        holder.binding.movieRating.numStars = voteAverage
        holder.binding.listener = recommendationsMovieListener
    }

    override fun getItemCount(): Int {
        return recommendationsResponse.results!!.size
    }

    class ViewHolder(var binding: FilmrecommendationslayoutBinding) : RecyclerView.ViewHolder(binding.root)
}
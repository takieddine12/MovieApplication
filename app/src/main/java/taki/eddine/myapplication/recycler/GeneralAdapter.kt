package taki.eddine.myapplication.recycler


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import taki.eddine.myapplication.R
import taki.eddine.myapplication.databinding.MovieslayoutBinding
import taki.eddine.myapplication.datamodels.MovieResultItem
import taki.eddine.myapplication.datamodels.MoviesDataModel
import taki.eddine.myapplication.movieslisteners.MovieListener

class GeneralAdapter(private val context: Context, private var movieListener: MovieListener)
    : PagedListAdapter<MovieResultItem, GeneralAdapter.viewHolder>(callback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding: MovieslayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.movieslayout, parent, false)
        return viewHolder(binding)
    }



    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val moviesModel = getItem(position)
        holder.binding.model = moviesModel


        holder.binding.movieRating.numStars = moviesModel!!.voteAverage?.toDouble()?.toInt()!!
        holder.binding.listener = movieListener
    }

    class viewHolder(val binding: MovieslayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItem(position: Int): MovieResultItem? {
        return super.getItem(position)
    }


    companion object {
        private val callback: DiffUtil.ItemCallback<MovieResultItem> = object : DiffUtil.ItemCallback<MovieResultItem>() {
            override fun areItemsTheSame(oldItem: MovieResultItem, newItem: MovieResultItem): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: MovieResultItem, newItem: MovieResultItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}
package taki.eddine.myapplication.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import taki.eddine.myapplication.R
import taki.eddine.myapplication.databinding.FavlayoutBinding
import taki.eddine.myapplication.datamodels.DataModel
import taki.eddine.myapplication.movieslisteners.FavMealListener

class Adapter(private val list : MutableList<DataModel>, private val context: Context, private var favMealListener: FavMealListener) : RecyclerView.Adapter<Adapter.viewHolder>() {
    private var adapterPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding: FavlayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.favlayout, parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val model = list[position]
        holder.binding.model = model
        holder.binding.favMovieRating.numStars  = model.voteAverage
        adapterPosition = position
        holder.binding.listener = favMealListener
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class viewHolder(val binding: FavlayoutBinding) : RecyclerView.ViewHolder(binding.root)

}
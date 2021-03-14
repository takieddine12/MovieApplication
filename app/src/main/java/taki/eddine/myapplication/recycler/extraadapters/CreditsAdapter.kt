package taki.eddine.myapplication.recycler.extraadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import taki.eddine.myapplication.R
import taki.eddine.myapplication.databinding.FilmcreditslayoutBinding
import taki.eddine.myapplication.datamodels.extramodels.credits.CreditsResponse
import taki.eddine.myapplication.movieslisteners.CreditsMovieListener

class CreditsAdapter(private val creditsResponse: CreditsResponse) : RecyclerView.Adapter<CreditsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: FilmcreditslayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.filmcreditslayout, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = creditsResponse.cast!![position]
    }

    override fun getItemCount(): Int {
        return creditsResponse.cast!!.size
    }

    class ViewHolder(val binding: FilmcreditslayoutBinding) : RecyclerView.ViewHolder(binding.root)
}
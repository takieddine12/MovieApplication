package taki.eddine.myapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.ybq.android.spinkit.style.ChasingDots
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import maes.tech.intentanim.CustomIntent.customType
import taki.eddine.myapplication.Extras
import taki.eddine.myapplication.databinding.NowplayingmoviesBinding
import taki.eddine.myapplication.datamodels.MovieResultItem
import taki.eddine.myapplication.movieslisteners.MovieListener
import taki.eddine.myapplication.mvvm.MainViewModel
import taki.eddine.myapplication.mvvm.MainViewModel.UiStates.SUCCESS
import taki.eddine.myapplication.mvvm.MainViewModel.UiStates.LOADING
import taki.eddine.myapplication.mvvm.MainViewModel.UiStates.ERROR
import taki.eddine.myapplication.recycler.GeneralAdapter
import taki.eddine.myapplication.ui.MoviesDetailsActivity
import timber.log.Timber

@AndroidEntryPoint
class NowPlayingMoviesFragment : Fragment() {
    private var job : Job? = null
    private val mainViewModel: MainViewModel by viewModels()
    private var adapter: GeneralAdapter? = null
    private var _binding: NowplayingmoviesBinding? = null
    private val  binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = NowplayingmoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nowMoviesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.nowMoviesRecycler.setHasFixedSize(true)
        adapter = GeneralAdapter(requireContext(), object : MovieListener {
            override fun sendMovieId(moviesDataClass: MovieResultItem?) {
                if(Extras.isNetworkConnected(requireContext())){
                    Intent(context, MoviesDetailsActivity::class.java).apply {
                        putExtra("movieId", moviesDataClass!!.id)
                        startActivity(this)
                        customType(requireActivity(),"fadein-to-fadeout")
                    }
                }
            }

        })

        if(Extras.isNetworkConnected(requireContext())){
            job = lifecycleScope.launchWhenCreated {
                mainViewModel.getNowPlayingMoviesInfo(Extras.getLanguageCode(requireContext())).collect {
                    when(it){
                        LOADING -> {
                        }
                        is SUCCESS -> {
                            it.data.observe(viewLifecycleOwner, Observer { liveData ->
                                adapter!!.submitList(liveData)
                                binding.nowMoviesRecycler.adapter = adapter
                            })
                        }
                        is ERROR -> {
                            Timber.d("Exception ${it.exception}")
                        }
                        else -> {}
                    }
                }
            }
        } else {
            mainViewModel.getPopularMoviesFromDB(Extras.NOWPLAY_VALue).observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter!!.submitList(it)
                    binding.nowMoviesRecycler.adapter = adapter
                }
            })
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        job?.cancel()
    }
}

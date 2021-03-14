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
import taki.eddine.myapplication.databinding.TopratedmovieslayoutBinding
import taki.eddine.myapplication.datamodels.MovieResultItem
import taki.eddine.myapplication.movieslisteners.MovieListener
import taki.eddine.myapplication.mvvm.MainViewModel
import taki.eddine.myapplication.ui.MoviesDetailsActivity
import taki.eddine.myapplication.mvvm.MainViewModel.UiStates.SUCCESS
import taki.eddine.myapplication.mvvm.MainViewModel.UiStates.LOADING
import taki.eddine.myapplication.mvvm.MainViewModel.UiStates.ERROR
import taki.eddine.myapplication.recycler.GeneralAdapter
import timber.log.Timber

@AndroidEntryPoint
class TopRatedMoviesFragment : Fragment() {
    private var job : Job? = null
    private var adapter: GeneralAdapter? = null
    private val mainViewModel: MainViewModel by viewModels()
    private var _binding: TopratedmovieslayoutBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = TopratedmovieslayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topRatedRecycle.layoutManager = LinearLayoutManager(requireContext())
        binding.topRatedRecycle.setHasFixedSize(true)
        adapter = GeneralAdapter(requireContext(),object : MovieListener{
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
                mainViewModel.getTopRatedMoviesInfo(Extras.getLanguageCode(requireContext())).collect {
                    when(it){
                        is LOADING -> {
                            binding.spinKit.setIndeterminateDrawable(ChasingDots())
                            binding.spinKit.visibility = View.VISIBLE
                        }
                        is SUCCESS -> {
                            binding.spinKit.visibility = View.INVISIBLE
                            it.data.observe(viewLifecycleOwner, Observer { liveData ->
                                adapter!!.submitList(liveData)
                                binding.topRatedRecycle.adapter = adapter
                            })

                        }
                        is ERROR -> {
                            binding.spinKit.visibility = View.INVISIBLE
                            Timber.d("Exception ${it.exception}")
                        }
                        else -> {}
                    }
                }
            }
        } else {
            mainViewModel.getPopularMoviesFromDB(Extras.TOPRATED_VALUE).observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter!!.submitList(it)
                    binding.topRatedRecycle.adapter = adapter
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
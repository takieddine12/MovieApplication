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
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.ybq.android.spinkit.style.ChasingDots
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import maes.tech.intentanim.CustomIntent.customType
import taki.eddine.myapplication.Extras
import taki.eddine.myapplication.PaginationUi.pagination.ItemDataSource
import taki.eddine.myapplication.databinding.PopularmovieslayoutBinding
import taki.eddine.myapplication.datamodels.MovieResultItem
import taki.eddine.myapplication.movieslisteners.MovieListener
import taki.eddine.myapplication.mvvm.MainViewModel
import taki.eddine.myapplication.mvvm.MainViewModel.UiStates.*
import taki.eddine.myapplication.network.ApiInterface
import taki.eddine.myapplication.recycler.GeneralAdapter
import taki.eddine.myapplication.ui.MoviesDetailsActivity
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class PopularMoviesFragment : Fragment() {

    @Inject
    lateinit var apiInterface: ApiInterface

    private var job: Job? = null
    private val mainViewModel: MainViewModel by viewModels()
    private var adapter: GeneralAdapter? = null
    private var _binding: PopularmovieslayoutBinding? = null
    private lateinit var compositeDisposable: CompositeDisposable
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = PopularmovieslayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.popularRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.popularRecycler.setHasFixedSize(true)
        adapter = GeneralAdapter(requireContext(), object : MovieListener {
            override fun sendMovieId(moviesDataClass: MovieResultItem?) {
                if (Extras.isNetworkConnected(requireContext())) {
                    Intent(context, MoviesDetailsActivity::class.java).apply {
                        putExtra("movieId", moviesDataClass!!.id)
                        startActivity(this)
                        customType(requireActivity(), "fadein-to-fadeout")
                    }
                }
            }

        })

        compositeDisposable = CompositeDisposable()

        if (Extras.isNetworkConnected(requireContext())) {
            job = lifecycleScope.launchWhenCreated {
                mainViewModel.getPopularMoviesInfo(
                        Extras.getLanguageCode(requireContext())).collect {
                    when (it) {
                        is LOADING -> {
                            binding.spinKit.visibility = View.VISIBLE
                        }
                        is SUCCESS -> {
                            binding.spinKit.visibility = View.INVISIBLE
                            it.data.observe(viewLifecycleOwner, Observer { liveData ->
                                liveData?.let {
                                    it.filter {
                                        if(it?.title == "Breach"){
                                            return@filter true
                                        }
                                        return@filter false
                                    }
                                    adapter!!.submitList(liveData)
                                    binding.popularRecycler.adapter = adapter
                                }
                            })
                        }
                        is ERROR -> {
                            binding.spinKit.visibility = View.INVISIBLE
                            Timber.d("Exception ${it.exception}")
                        }

                        else -> {
                        }
                    }
                }
            }
        } else {
            mainViewModel.getPopularMoviesFromDB(Extras.POPULAR_VALUE).observe(viewLifecycleOwner, Observer {
                adapter!!.submitList(it)
                binding.popularRecycler.adapter = adapter
            })
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        job?.cancel()
    }
}
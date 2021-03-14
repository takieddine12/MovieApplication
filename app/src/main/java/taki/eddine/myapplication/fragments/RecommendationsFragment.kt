package taki.eddine.myapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import maes.tech.intentanim.CustomIntent.customType
import taki.eddine.myapplication.BuildConfig
import taki.eddine.myapplication.Extras
import taki.eddine.myapplication.recycler.extraadapters.RecommendationsAdapter
import taki.eddine.myapplication.databinding.MovierecommendationlayoutBinding
import taki.eddine.myapplication.movieslisteners.RecommendationsMovieListener
import taki.eddine.myapplication.mvvm.MainViewModel
import taki.eddine.myapplication.mvvm.SharedViewModel
import taki.eddine.myapplication.ui.MoviesDetailsActivity
import timber.log.Timber

@AndroidEntryPoint
class RecommendationsFragment : Fragment() {
    private var compositeDisposable: CompositeDisposable? = null
    private val mainViewModel: MainViewModel by viewModels()
    private var sharedViewModel: SharedViewModel? = null
    private var _binding: MovierecommendationlayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = MovierecommendationlayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recommendationsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recommendationsRecycler.setHasFixedSize(true)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        compositeDisposable = CompositeDisposable()

        sharedViewModel!!.movieId.observe(viewLifecycleOwner, Observer {
            getMovieRecommendations(it)
        })

    }

    private fun getMovieRecommendations(movieId: String?) {
       mainViewModel.getMovieRecommendations(movieId, BuildConfig.MoviesApiKey, Extras.getLanguageCode(requireContext()), 1)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe {
                    compositeDisposable?.add(it)
                }
                ?.subscribe ({ recommendationResponse ->
                    if(!recommendationResponse?.results.isNullOrEmpty()){
                        val recommendationsAdapter = RecommendationsAdapter(recommendationResponse!!,object : RecommendationsMovieListener{
                            override fun onRecommendedMovie(movieId: Int) {
                                Intent(requireContext(),MoviesDetailsActivity::class.java).apply {
                                    putExtra("movieId",movieId)
                                    startActivity(this)
                                    customType(requireActivity(),"fadein-to-fadeout")
                                }
                            }
                        })
                        binding.recommendationsRecycler.adapter = recommendationsAdapter
                        binding.recommendationsText.visibility = View.GONE
                    } else {
                        binding.recommendationsText.visibility = View.VISIBLE
                    }

                },{ t: Throwable? ->
                    Timber.d("Recommendations Exception ${t?.message}")
                })!!

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        compositeDisposable!!.dispose()
    }
}
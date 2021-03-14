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
import taki.eddine.myapplication.recycler.extraadapters.ReviewsAdapter
import taki.eddine.myapplication.databinding.MoviereviewslayoutBinding
import taki.eddine.myapplication.movieslisteners.ReadMoreListener
import taki.eddine.myapplication.mvvm.MainViewModel
import taki.eddine.myapplication.mvvm.SharedViewModel
import taki.eddine.myapplication.ui.ReadMoreActivity
import timber.log.Timber

@AndroidEntryPoint
class ReviewsFragment : Fragment() {
    private var sharedViewModel: SharedViewModel? = null
    private val mainViewModel: MainViewModel by viewModels()
    private var _binding : MoviereviewslayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var compositeDisposable: CompositeDisposable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = MoviereviewslayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compositeDisposable  = CompositeDisposable()

        binding.reviewsRecycler.layoutManager = LinearLayoutManager(context)
        binding.reviewsRecycler.setHasFixedSize(true)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel!!.movieId.observe(viewLifecycleOwner, Observer {
            getMovieExtras(it)
        })

    }

    private fun getMovieExtras(movieId: String) {
        Timber.d("MovieId $movieId")
        mainViewModel.getMovieExtras(movieId, BuildConfig.MoviesApiKey, Extras.getLanguageCode(requireContext()), 1)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe {
                    compositeDisposable.add(it)
                }
                ?.subscribe ({ reviewsResponse ->
                    if(!reviewsResponse?.results.isNullOrEmpty()){
                        val reviewsAdapter = ReviewsAdapter(reviewsResponse!!,object : ReadMoreListener{
                            override fun readMore(review: String, createdAt: String, updateAt: String) {
                                Intent(requireContext(),ReadMoreActivity::class.java).apply {
                                    putExtra("review",review)
                                    putExtra("createdAt",createdAt)
                                    putExtra("updateAt",updateAt)
                                    startActivity(this)
                                    customType(requireActivity(),"fadein-to-fadeout")
                                }
                            }
                        })
                        binding.reviewsRecycler.adapter = reviewsAdapter
                        binding.reviewsText.visibility = View.GONE
                    } else {
                        binding.reviewsText.visibility = View.VISIBLE
                    }

                },{
                    Timber.d("Reviews Exception ${it.message}")
                })!!

    }


    override fun onDestroy() {
        super.onDestroy()
       compositeDisposable.dispose()
        _binding = null
    }
}
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
import taki.eddine.myapplication.recycler.extraadapters.CreditsAdapter
import taki.eddine.myapplication.databinding.MoviecreditslayoutBinding
import taki.eddine.myapplication.movieslisteners.CreditsMovieListener
import taki.eddine.myapplication.mvvm.MainViewModel
import taki.eddine.myapplication.mvvm.SharedViewModel
import taki.eddine.myapplication.ui.ActorInfoActivity
import timber.log.Timber

@AndroidEntryPoint
class CreditsFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private var sharedViewModel: SharedViewModel? = null
    private var _binding: MoviecreditslayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var compositeDisposable : CompositeDisposable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = MoviecreditslayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.creditsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.creditsRecycler.setHasFixedSize(true)

        compositeDisposable = CompositeDisposable()

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel!!.movieId.observe(viewLifecycleOwner, Observer {
            getMovieCredits(it)
        })

    }

    private fun getMovieCredits(movieId: String?) {
          mainViewModel.getMovieCredits(movieId,BuildConfig.MoviesApiKey, Extras.getLanguageCode(requireContext()))
                 ?.subscribeOn(Schedulers.io())
                 ?.observeOn(AndroidSchedulers.mainThread())
                 ?.doOnSubscribe {
                     compositeDisposable.add(it)
                 }
                 ?.subscribe ({ creditsResponse ->
                     if(!creditsResponse?.cast.isNullOrEmpty()){
                         val creditsAdapter =CreditsAdapter(creditsResponse!!)
                         binding.creditsRecycler.adapter = creditsAdapter
                         binding.creditsText.visibility = View.INVISIBLE
                     } else {
                         binding.creditsText.visibility = View.VISIBLE
                     }
                 },{ t: Throwable? ->
                     Timber.d("Credits Exception ${t?.message}")
                 })!!

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding  = null
        compositeDisposable.dispose()
    }
}
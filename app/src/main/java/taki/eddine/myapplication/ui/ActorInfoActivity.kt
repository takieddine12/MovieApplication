package taki.eddine.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import taki.eddine.myapplication.BuildConfig
import taki.eddine.myapplication.Extras
import taki.eddine.myapplication.databinding.ActivityActorInfoBinding
import taki.eddine.myapplication.mvvm.MainViewModel
import timber.log.Timber

@AndroidEntryPoint
class ActorInfoActivity : AppCompatActivity() {
    private val mainViewModel : MainViewModel by viewModels()
    private var _binding : ActivityActorInfoBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding  = ActivityActorInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        intent?.let {
            Timber.d("received Intent ${it.getIntExtra("creditId",0)}")
            val creditId = it.getIntExtra("creditId",0)
            mainViewModel.getActorInfo(creditId,BuildConfig.MoviesApiKey, Extras.getLanguageCode(this)!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({ actorResponse ->
                        binding.model = actorResponse
                        Timber.d("OnSubscribe ${actorResponse.birthday}")
                    },{
                        Timber.d("Actor Exception ${it.message}")
                    })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
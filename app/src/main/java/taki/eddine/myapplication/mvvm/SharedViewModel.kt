package taki.eddine.myapplication.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val liveData = MutableLiveData<String>()
    fun setMovieId(movieId: String) {
        liveData.value = movieId
    }

    val movieId: LiveData<String>
        get() = liveData
}
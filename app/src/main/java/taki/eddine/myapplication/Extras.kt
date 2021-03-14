package taki.eddine.myapplication

import android.content.Context
import android.net.ConnectivityManager

object Extras {

    fun getLanguageCode(context: Context): String? {
        val languagesPrefs = context.getSharedPreferences("languages", Context.MODE_PRIVATE)
        return languagesPrefs.getString("languageCode", "en-US")
    }

    fun isNetworkConnected(c : Context): Boolean {
        val connectivityManager = c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    const val POPULAR_VALUE = "popularMovie"
    const val UPCOMING_VALUE ="upComingMovie"
    const val TOPRATED_VALUE = "topRatedMovie"
    const val NOWPLAY_VALue = "nowPlayingMovie"


    // TODO : Boudary Callbacks
    const val POPULAR_PREFS ="popular_prefs"
    const val UPCOMING_PREFS = "upcoming_prefs"
    const val TOPRATED_PREFS = "toprated_prefs"
    const val NOWPLAY_PREFS ="nowplaying_prefs"


}
package taki.eddine.myapplication.imgbindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import okio.ByteString.Companion.toByteString
import taki.eddine.myapplication.R

object GeneralImgBinding {

    @BindingAdapter("DetailsImg")
    @JvmStatic
    fun setImgUrl(view: ImageView, Url: String?) {
        Picasso.get().load("https://image.tmdb.org/t/p/w185//$Url").fit().into(view)
    }

    @BindingAdapter("FavImg")
    @JvmStatic
    fun setFavImage(view: ImageView, url: String?) {
        Picasso.get().load(url).fit().into(view)
    }

    @BindingAdapter("PlayNowImg")
    @JvmStatic
    fun setPlayNowMovieImage(view: ImageView, url: String?) {
        Picasso.get().load("https://image.tmdb.org/t/p/w185//$url").fit().into(view)

    }

    @BindingAdapter("userProfileIcon")
    @JvmStatic
    fun setUserProfileIcon(view: ImageView, movieUrl: String?) {

        if(movieUrl.isNullOrEmpty()){
            Picasso.get().load("https://uxwing.com/wp-content/themes/uxwing/download/12-people-gesture/profile-boy.png").fit().into(view)
        } else if(!movieUrl.contains("gravatar") && !movieUrl.isNullOrEmpty()){
            Picasso.get().load("https://image.tmdb.org/t/p/w185//$movieUrl").fit().into(view)
        } else {
            Picasso.get().load(movieUrl.substring(1)).error(R.drawable.ic_profilesvg)
                    .fit().into(view)
        }
    }
}
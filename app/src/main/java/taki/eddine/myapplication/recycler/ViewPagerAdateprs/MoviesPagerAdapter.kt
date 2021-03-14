package taki.eddine.myapplication.recycler.ViewPagerAdateprs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import taki.eddine.myapplication.fragments.NowPlayingMoviesFragment
import taki.eddine.myapplication.fragments.PopularMoviesFragment
import taki.eddine.myapplication.fragments.TopRatedMoviesFragment
import taki.eddine.myapplication.fragments.UpcomingMoviesFragment


class MoviesPagerAdapter(fm : FragmentManager,behaviour : Int) : FragmentPagerAdapter(fm,behaviour) {
    private val tabs = 4
    override fun getCount(): Int {
        return  tabs
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return PopularMoviesFragment()
            1 -> return UpcomingMoviesFragment()
            2 -> return TopRatedMoviesFragment()
            3 -> return NowPlayingMoviesFragment()
        }
        return PopularMoviesFragment()
    }

    override fun getItemPosition(`object`: Any): Int {
        return  POSITION_NONE
    }
}

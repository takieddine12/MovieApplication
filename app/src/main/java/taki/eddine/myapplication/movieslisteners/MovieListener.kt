package taki.eddine.myapplication.movieslisteners

import taki.eddine.myapplication.datamodels.MovieResultItem

interface MovieListener {
    fun sendMovieId(moviesDataClass: MovieResultItem?)
}
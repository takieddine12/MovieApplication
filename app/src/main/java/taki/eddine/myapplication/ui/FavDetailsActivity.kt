package taki.eddine.myapplication.ui

import android.annotation.SuppressLint
import android.content.Intent
import taki.eddine.myapplication.R
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.awesomedialog.*
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import taki.eddine.myapplication.recycler.Adapter
import taki.eddine.myapplication.databinding.ActivityFavdetailsBinding
import taki.eddine.myapplication.datamodels.DataModel
import taki.eddine.myapplication.movieslisteners.FavMealListener
import taki.eddine.myapplication.mvvm.MainViewModel
import java.util.*

@AndroidEntryPoint
class FavDetailsActivity : AppCompatActivity() {
    private var mainViewModel: MainViewModel? = null
    private var adapter: Adapter? = null
    private  var list: MutableList<DataModel>? = null
    private var _binding: ActivityFavdetailsBinding? = null
    private lateinit var compositeDisposable: CompositeDisposable
    private val binding get() = _binding!!

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavdetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.favrecycler.layoutManager = LinearLayoutManager(this)
        binding.favrecycler.setHasFixedSize(true)
        list = ArrayList()
        compositeDisposable  = CompositeDisposable()
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        deleteMovieOnSwipe()

        mainViewModel!!.deleteDuplicateData()
                ?.subscribeOn(Schedulers.io())
                ?.subscribe {}


        mainViewModel!!.getAllSavedMovies().observe(this) { dataList ->
            if (dataList != null &&  dataList.isNotEmpty()) {
                binding.noMoviesText.visibility = View.GONE
                list = dataList

            } else {
                list = dataList
                binding.noMoviesText.visibility = View.VISIBLE
            }
            adapter = Adapter(list!!, this@FavDetailsActivity, object : FavMealListener {
                override fun sendFavId(dataModel: DataModel?) {
                    Intent(this@FavDetailsActivity, MoviesDetailsActivity::class.java).apply {
                        putExtra("movieId", dataModel!!.movieId)
                        startActivity(this)
                    }
                }
            })
            binding.favrecycler.adapter = adapter
        }

        binding.deleteAll.setOnClickListener {
            if (list != null && list!!.isNotEmpty()) {
                binding.noMoviesText.visibility = View.GONE
                AwesomeDialog.build(this)
                        .title(
                                getString(R.string.deleteall),
                                titleColor = ContextCompat.getColor(this, android.R.color.black),
                                fontStyle = ResourcesCompat.getFont(this, R.font.rubik_medium))
                        .body(getString(R.string.confirmationdeletion),
                                color = ContextCompat.getColor(this, android.R.color.black),
                                fontStyle = ResourcesCompat.getFont(this, R.font.rubik_medium))
                        .icon(R.drawable.ic_dialog_delete, animateIcon = true)
                        .position(AwesomeDialog.POSITIONS.BOTTOM)
                        .onPositive(getString(R.string.delete)) {
                            mainViewModel!!.deleteAllMovies()
                                    ?.subscribeOn(Schedulers.io())
                                    ?.subscribe(object : CompletableObserver {
                                        override fun onSubscribe(d: Disposable) {
                                            compositeDisposable.add(d)
                                        }
                                        override fun onComplete() {}
                                        override fun onError(e: Throwable) {}
                                    })
                        }
                        .onNegative(getString(R.string.cancel)) {}
                        .show()
            } else {
                binding.noMoviesText.visibility = View.VISIBLE
            }
        }
    }

    private fun deleteMovieOnSwipe() {
        val simpleCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mainViewModel!!.deletePerMovie(list!![viewHolder.adapterPosition])
                        ?.subscribeOn(Schedulers.io())
                        ?.subscribe(object : CompletableObserver {
                            override fun onSubscribe(d: Disposable) {
                                compositeDisposable.add(d)
                            }

                            override fun onComplete() {}
                            override fun onError(e: Throwable) {}
                        })
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.favrecycler)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        _binding = null

    }

}
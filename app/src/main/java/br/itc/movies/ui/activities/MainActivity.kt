package br.itc.movies.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.itc.movies.R
import br.itc.movies.data.api.MovieDB
import br.itc.movies.data.api.MovieDBClient
import br.itc.movies.data.repositories.NetworkState
import br.itc.movies.ui.adapters.MoviePagedListAdapter
import br.itc.movies.ui.repositories.MoviePagedListRepository
import br.itc.movies.ui.viewmodels.MainActivityViewModel
import com.google.android.material.textview.MaterialTextView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: MaterialTextView

    lateinit var movieRepository: MoviePagedListRepository
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.movie_list)
        progressBar = findViewById(R.id.progress_bar_popular)
        errorTextView = findViewById(R.id.error_popular)

        val apiService: MovieDB = MovieDBClient.getClient()
        movieRepository = MoviePagedListRepository(apiService)
        viewModel = getViewModel()

        val movieAdapter = MoviePagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 2)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                // MOVIE_VIEW_TYPE needs 1 out of 2 span, NETWORK_VIEW_TYPE - all 2 span:
                if (viewType == movieAdapter.MOVIE_VIEW_TYPE) return 1 else return 2
            }
        }

        recyclerView.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            adapter = movieAdapter
        }

        viewModel.moviePagedList.observe(this, Observer { movieAdapter.submitList(it) })
        viewModel.networkState.observe(this, Observer {
            progressBar.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE

            errorTextView.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) movieAdapter.setNetworkState(it)
        })
    }

    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(movieRepository) as T
            }
        })[MainActivityViewModel::class.java]
    }
}
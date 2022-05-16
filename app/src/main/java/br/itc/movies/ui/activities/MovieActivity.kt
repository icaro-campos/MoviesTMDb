package br.itc.movies.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.itc.movies.R
import br.itc.movies.data.MovieDetails
import br.itc.movies.data.api.MovieDB
import br.itc.movies.data.api.MovieDBClient
import br.itc.movies.data.api.POSTER_BASE_URL
import br.itc.movies.data.repositories.NetworkState
import br.itc.movies.ui.repositories.MovieDetailsRepository
import br.itc.movies.ui.viewmodels.MovieViewModel
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textview.MaterialTextView
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.*

class MovieActivity : AppCompatActivity() {

    private lateinit var movieRepository: MovieDetailsRepository
    private lateinit var viewModel: MovieViewModel

    private lateinit var progressIndicator: CircularProgressIndicator
    private lateinit var error: MaterialTextView
    private lateinit var title: MaterialTextView
    private lateinit var subtitle: MaterialTextView
    private lateinit var releaseDate: MaterialTextView
    private lateinit var overview: MaterialTextView
    private lateinit var language: MaterialTextView
    private lateinit var genre: MaterialTextView
    private lateinit var moviePoster: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        initComponents()

        val movieId = intent.getIntExtra("id", 1)
        val apiService: MovieDB = MovieDBClient.getClient()

        movieRepository = MovieDetailsRepository(apiService)
        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer { bindUI(it) })
        viewModel.networkState.observe(this, Observer {
            progressIndicator.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE

            error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun initComponents() {
        progressIndicator = findViewById(R.id.progress_indicator)
        error = findViewById(R.id.error)
        title = findViewById(R.id.title)
        subtitle = findViewById(R.id.subtitle)
        releaseDate = findViewById(R.id.release_date)
        overview = findViewById(R.id.overview)
        moviePoster = findViewById(R.id.movie_poster)
        language = findViewById(R.id.language)
        genre = findViewById(R.id.genres)
    }

    fun bindUI(it: MovieDetails) {
        title.text = it.title
        subtitle.text = it.tagline
        overview.text = it.overview
        language.text = it.originalLanguage
        releaseDate.text = it.releaseDate
        genre.text = it.genres.joinToString { it.name }

        val posterURL = POSTER_BASE_URL + it.posterPath

        Glide.with(this).load(posterURL).into(moviePoster)
    }

    private fun getViewModel(movieId: Int): MovieViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {

            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieViewModel(movieRepository, movieId) as T
            }
        })[MovieViewModel::class.java]
    }
}
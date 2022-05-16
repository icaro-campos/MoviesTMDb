package br.itc.movies.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.itc.movies.data.MovieDetails
import br.itc.movies.data.repositories.NetworkState
import br.itc.movies.ui.repositories.MovieDetailsRepository
import io.reactivex.disposables.CompositeDisposable

class MovieViewModel(
    private val movieRepository: MovieDetailsRepository,
    movieId: Int
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val movieDetails: LiveData<MovieDetails> by lazy {
        movieRepository.fetchMovieDetails(compositeDisposable, movieId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()
    }
}
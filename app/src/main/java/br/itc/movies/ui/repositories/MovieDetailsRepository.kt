package br.itc.movies.ui.repositories

import androidx.lifecycle.LiveData
import br.itc.movies.data.MovieDetails
import br.itc.movies.data.api.MovieDB
import br.itc.movies.data.repositories.MovieDetailsNetworkDataSource
import br.itc.movies.data.repositories.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService: MovieDB) {
    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchMovieDetails(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<MovieDetails> {
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieDetailsResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }
}
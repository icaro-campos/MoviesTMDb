package br.itc.movies.data.repositories

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import br.itc.movies.data.Movie
import br.itc.movies.data.api.MovieDB
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(
    private val apiService: MovieDB,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource: MutableLiveData<MovieDataSource> by lazy { MutableLiveData<MovieDataSource>() }

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService, compositeDisposable)
        moviesLiveDataSource.postValue(movieDataSource)

        return movieDataSource
    }
}
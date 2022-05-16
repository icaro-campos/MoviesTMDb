package br.itc.movies.ui.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import br.itc.movies.data.Movie
import br.itc.movies.data.api.MovieDB
import br.itc.movies.data.api.POST_PER_PAGE
import br.itc.movies.data.repositories.MovieDataSource
import br.itc.movies.data.repositories.MovieDataSourceFactory
import br.itc.movies.data.repositories.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository(private val apiService: MovieDB) {

    lateinit var movieDataSourceFactory: MovieDataSourceFactory
    lateinit var moviePagedList: LiveData<PagedList<Movie>>

    fun fetchMoviePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Movie>> {
        movieDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(movieDataSourceFactory, config).build()
        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            movieDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState
        )
    }
}
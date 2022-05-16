package br.itc.movies.data.repositories

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import br.itc.movies.data.Movie
import br.itc.movies.data.api.FIRST_PAGE
import br.itc.movies.data.api.MovieDB
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource(
    private val apiService: MovieDB,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Movie>() {

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    private var page = FIRST_PAGE

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getNowPlayingMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    callback.onResult(it.movieList, null, page + 1)
                    networkState.postValue(NetworkState.LOADED)
                }, {
                    networkState.postValue(NetworkState.ERROR)
                })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getNowPlayingMovie(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe({

                    if (it.totalPages >= params.key) {
                        callback.onResult(it.movieList, params.key + 1)
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        networkState.postValue(NetworkState.ENDOFLIST)
                    }
                }, {
                    networkState.postValue(NetworkState.ERROR)
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

    }
}
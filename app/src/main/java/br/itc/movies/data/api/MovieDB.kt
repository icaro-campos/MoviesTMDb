package br.itc.movies.data.api

import br.itc.movies.data.MovieDetails
import br.itc.movies.data.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDB {

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>

    @GET("movie/now_playing")
    fun getNowPlayingMovie(@Query("page") page: Int): Single<MovieResponse>
}
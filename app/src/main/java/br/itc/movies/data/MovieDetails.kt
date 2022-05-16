package br.itc.movies.data

import com.google.gson.annotations.SerializedName

data class MovieDetails(

    val id: Int,
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val tagline: String,
    val title: String,
    val genres: List<Genre>

)

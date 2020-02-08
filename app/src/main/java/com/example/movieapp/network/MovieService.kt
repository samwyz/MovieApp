package com.example.movieapp.network

import com.example.movieapp.network.model.SearchResult
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface MovieService {

    @GET("?apiKey=$API_KEY")
    fun searchMovies(@Query("s") title: String): Call<SearchResult>

    companion object {
        private const val OMDB_URL = "https://www.omdbapi.com/"
        private const val API_KEY = "1c157b05"

        fun create(): MovieService {
            val retrofit = Retrofit.Builder()
                .baseUrl(OMDB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(MovieService::class.java)
        }
    }
}
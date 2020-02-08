package com.example.movieapp.network.model

import com.google.gson.annotations.SerializedName


data class SearchResult(
    @SerializedName("Search") val movieList: List<Movie>?
)
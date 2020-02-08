package com.example.movieapp.network.model

import com.google.gson.annotations.SerializedName


data class Movie(
    @SerializedName("Title") val title: String,
    @SerializedName("Poster")val poster: String
)
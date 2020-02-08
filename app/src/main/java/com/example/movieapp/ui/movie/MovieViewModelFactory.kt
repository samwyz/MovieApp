package com.example.movieapp.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.network.MovieService

/**
 * Copyright (c) 2020 Under Armour. All rights reserved.
 */
class MovieViewModelFactory(private val movieService: MovieService) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MovieService::class.java).newInstance(movieService)
    }
}
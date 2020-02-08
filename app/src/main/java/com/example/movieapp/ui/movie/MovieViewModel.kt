package com.example.movieapp.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.network.MovieService
import com.example.movieapp.network.Resource
import com.example.movieapp.network.model.SearchResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieViewModel(private val movieService: MovieService) : ViewModel() {

    private val _movies = MutableLiveData<Resource<SearchResult?>>()
    val movies get() = _movies as LiveData<Resource<SearchResult?>>

    /**
     * Get those movies! Passing in a [CoroutineDispatcher] here
     * to make testing easier. Normally I would create a DispatcherProvider,
     * inject it into this class via constructor,and replace it with
     * a TestDispatcherProvider in tests for a cleaner implementation
     */
    fun searchMovies(title: String, dispatcher: CoroutineDispatcher) {
        viewModelScope.launch(dispatcher) {
            _movies.postValue(Resource.Loading())

            movieService.searchMovies(title).enqueue(object : Callback<SearchResult> {

                override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                    _movies.postValue(Resource.Failure(t))
                }

                override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                    _movies.postValue(Resource.Success(response.body()))
                }
            })
        }
    }
}

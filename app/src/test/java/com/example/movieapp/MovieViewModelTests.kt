package com.example.movieapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.movieapp.network.MovieService
import com.example.movieapp.network.Resource
import com.example.movieapp.network.model.Movie
import com.example.movieapp.network.model.SearchResult
import com.example.movieapp.ui.movie.MovieViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@ExperimentalCoroutinesApi
class MovieViewModelTests {

    // allows instant execution of livedata
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // allows instant execution of coroutines
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val liveDataCaptor: ArgumentCaptor<Resource<SearchResult?>> = MockProvider.argumentCaptor()
    private val callbackCaptor: ArgumentCaptor<Callback<SearchResult>> = MockProvider.argumentCaptor()
    private val observer: Observer<Resource<SearchResult?>> = MockProvider.mock()
    private val mockService: MovieService = MockProvider.mock()
    private val mockCall: Call<SearchResult> = MockProvider.mock()
    private val moviesViewModel = MovieViewModel(mockService)

    @Before
    fun setup() {
        moviesViewModel.movies.observeForever(observer)
    }

    @After
    fun teardown() {
        moviesViewModel.movies.removeObserver(observer)
    }

    @Test
    fun `search triggers network call, successful result triggers observer`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        // setup to return a mock call from retrofit service
        `when`(mockService.searchMovies("test")).thenReturn(mockCall)

        // run the search
        moviesViewModel.searchMovies("test", coroutinesTestRule.testDispatcher)

        // capture the callback passed into the enqueue function and invoke response
        callbackCaptor.run {
            verify(mockCall, times(1)).enqueue(capture())
            value.onResponse(MockProvider.mock(), Response.success(SearchResult(listOf(testMovie))))
        }

        // capture and validate the resulting livedata
        liveDataCaptor.run {
            verify(observer, atLeastOnce()).onChanged(capture())
            assertEquals(testMovie.title, (value as Resource.Success).data?.movieList?.get(0)?.title)
        }
    }

    @Test
    fun `search triggers network call, failed result triggers observer`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        // setup to return a mock call from retrofit service
        `when`(mockService.searchMovies("test")).thenReturn(mockCall)

        // run the search
        moviesViewModel.searchMovies("test", coroutinesTestRule.testDispatcher)

        // capture the callback passed into the enqueue function and invoke failure
        callbackCaptor.run {
            verify(mockCall, times(1)).enqueue(capture())
            value.onFailure(MockProvider.mock(), testException)
        }

        // capture and validate the resulting livedata
        liveDataCaptor.run {
            verify(observer, atLeastOnce()).onChanged(capture())
            assertEquals(testException, (value as Resource.Failure).throwable)
        }
    }

    companion object {
        private val testMovie = Movie("Test", "testUrl")
        private val testException = Exception()
    }
}
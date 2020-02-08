package com.example.movieapp.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.argument
import com.example.movieapp.network.MovieService
import com.example.movieapp.network.Resource
import com.example.movieapp.network.model.SearchResult
import kotlinx.android.synthetic.main.fragment_movie.movie_empty_state
import kotlinx.android.synthetic.main.fragment_movie.movie_progress
import kotlinx.android.synthetic.main.fragment_movie.movie_recycler
import kotlinx.coroutines.Dispatchers

class MovieFragment : Fragment() {

    private val movieService = MovieService.create()
    private val viewModel: MovieViewModel by activityViewModels { MovieViewModelFactory(movieService) }
    private val adapter = MovieRecyclerAdapter()

    private var movieTitle: String by argument()
    private var limit: Int by argument()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movie_recycler.adapter = adapter
        movie_recycler.layoutManager = LinearLayoutManager(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.movies.observe(viewLifecycleOwner, Observer(::showMovies))
        viewModel.searchMovies(movieTitle, Dispatchers.Default)
    }

    private fun showMovies(result: Resource<SearchResult?>) {
        when (result) {
            is Resource.Loading -> {
                movie_recycler.visibility = View.GONE
                movie_empty_state.visibility = View.GONE
                movie_progress.visibility = View.VISIBLE
            }
            is Resource.Failure -> {
                movie_empty_state.text = "Uh oh: ${result.throwable.message} "

                movie_recycler.visibility = View.GONE
                movie_progress.visibility = View.GONE
                movie_empty_state.visibility = View.VISIBLE
            }
            is Resource.Success -> {
                val movieList = result.data?.movieList
                if (movieList.isNullOrEmpty()) {
                    movie_empty_state.text = "No Movies! "

                    movie_recycler.visibility = View.GONE
                    movie_progress.visibility = View.GONE
                    movie_empty_state.visibility = View.VISIBLE
                } else {
                    val limitList = movieList.take(limit)
                    adapter.setMovieList(limitList)

                    movie_progress.visibility = View.GONE
                    movie_empty_state.visibility = View.GONE
                    movie_recycler.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        fun newInstance(movieTitle: String, limit: Int) =
            MovieFragment().apply {
                this.movieTitle = movieTitle
                this.limit = limit
            }
    }
}

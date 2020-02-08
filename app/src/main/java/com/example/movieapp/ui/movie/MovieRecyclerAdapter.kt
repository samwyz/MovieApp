package com.example.movieapp.ui.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.network.model.Movie
import kotlinx.android.synthetic.main.item_movie.view.movie_poster
import kotlinx.android.synthetic.main.item_movie.view.movie_title


class MovieRecyclerAdapter(private var movieList: List<Movie> = emptyList()) : RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    /**
     * Swap out the movie list. Complete implementation would
     * leverage DiffUtil to efficiently swap out only the items
     * that have changed.
     */
    fun setMovieList(movieList: List<Movie>) {
        this.movieList = movieList
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movie: Movie) {
            Glide.with(itemView)
                .clear(itemView.movie_poster)

            Glide.with(itemView)
                .load(movie.poster)
                .into(itemView.movie_poster)

            itemView.movie_title.text = movie.title
        }
    }
}
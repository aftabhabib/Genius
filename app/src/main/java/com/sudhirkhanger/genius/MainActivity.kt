/*
 * Copyright 2018 Sudhir Khanger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sudhirkhanger.genius

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val COL = 2
    }

    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var movieAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        callGetPopularMovies()

        viewManager = GridLayoutManager(this, COL)
        movieAdapter = MovieAdapter(mutableListOf(),
                object : MovieAdapter.OnMovieClickListener {
                    override fun invoke(movie: Movie) {
                        Log.e(TAG, movie.title)
                    }
                })

        movieRecyclerView = findViewById<RecyclerView>(R.id.movie_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = movieAdapter
        }
    }

    private fun callGetPopularMovies() {
        val theMovieDbService = TheMovieDbService.create()
        val call = theMovieDbService.getPopularMovies(1, BuildConfig.THE_MOVIE_DB_API_KEY)
        call.enqueue(object : Callback<MovieList> {

            override fun onResponse(call: Call<MovieList>?, response: Response<MovieList>?) {
                Log.e(TAG, response?.body()?.results!![0]?.title)
                val movieList: List<Movie?>? = response.body()?.results
                val movieAdapter = MovieAdapter(movieList!!.toMutableList(),
                        object : MovieAdapter.OnMovieClickListener {
                            override fun invoke(movie: Movie) {
                                Log.e(TAG, movie.title)
                            }
                        })
                movieRecyclerView.adapter = movieAdapter
            }

            override fun onFailure(call: Call<MovieList>?, t: Throwable?) {
            }
        })
    }
}

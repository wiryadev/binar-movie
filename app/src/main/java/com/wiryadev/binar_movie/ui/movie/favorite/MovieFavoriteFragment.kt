package com.wiryadev.binar_movie.ui.movie.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wiryadev.binar_movie.databinding.FragmentMovieFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieFavoriteFragment : Fragment() {

    private var _binding: FragmentMovieFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieFavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val moviesAdapter = MovieFavoriteAdapter {
            findNavController().navigate(
                MovieFavoriteFragmentDirections.actionNavigationMovieFavoriteToNavigationDetailMovie(
                    movieId = it
                )
            )
        }

        with(binding) {
            rvMovies.apply {
                adapter = moviesAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            moviesAdapter.submitList(it.movies)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
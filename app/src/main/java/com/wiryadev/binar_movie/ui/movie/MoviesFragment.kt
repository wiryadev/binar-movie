package com.wiryadev.binar_movie.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.wiryadev.binar_movie.databinding.FragmentMoviesBinding
import com.wiryadev.binar_movie.ui.adapter.LoadingStateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val moviesAdapter = MoviesAdapter()
        with(binding) {
            rvMovies.apply {
                adapter = moviesAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        moviesAdapter.retry()
                    }
                )
                layoutManager = LinearLayoutManager(context)
            }

            moviesAdapter.loadStateFlow.asLiveData().observe(viewLifecycleOwner) { loadState ->
                // Show loading spinner during initial load or refresh.
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            }
        }
        viewModel.movies.observe(viewLifecycleOwner) {
            moviesAdapter.submitData(lifecycle, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
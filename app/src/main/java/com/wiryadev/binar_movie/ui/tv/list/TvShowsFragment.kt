package com.wiryadev.binar_movie.ui.tv.list

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
import com.wiryadev.binar_movie.databinding.FragmentTvShowsBinding
import com.wiryadev.binar_movie.ui.adapter.LoadingStateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowsFragment : Fragment() {

    private var _binding: FragmentTvShowsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TvShowsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvShowsAdapter = TvShowsAdapter()
        with(binding) {
            rvTvShows.apply {
                adapter = tvShowsAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        tvShowsAdapter.retry()
                    }
                )
                layoutManager = LinearLayoutManager(context)
            }

            tvShowsAdapter.loadStateFlow.asLiveData().observe(viewLifecycleOwner) { loadState ->
                // Show loading spinner during initial load or refresh.
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            }
        }

        viewModel.movies.observe(viewLifecycleOwner) {
            tvShowsAdapter.submitData(lifecycle, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
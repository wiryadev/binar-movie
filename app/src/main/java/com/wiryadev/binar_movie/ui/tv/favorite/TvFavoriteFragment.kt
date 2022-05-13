package com.wiryadev.binar_movie.ui.tv.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wiryadev.binar_movie.databinding.FragmentTvFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvFavoriteFragment : Fragment() {

    private var _binding: FragmentTvFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TvFavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvAdapter = TvFavoriteAdapter {
            findNavController().navigate(
                TvFavoriteFragmentDirections.actionNavigationTvFavoriteToNavigationDetailTv(
                    tvId = it
                )
            )
        }

        with(binding) {
            rvTvShows.apply {
                adapter = tvAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            tvAdapter.submitList(it.tvShows)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
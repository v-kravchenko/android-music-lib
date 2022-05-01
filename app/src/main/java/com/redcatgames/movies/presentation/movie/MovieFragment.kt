package com.redcatgames.movies.presentation.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.redcatgames.movies.databinding.HomeFragmentBinding
import com.redcatgames.movies.databinding.MovieFragmentBinding
import com.redcatgames.movies.presentation.base.BaseFragment
import com.redcatgames.movies.presentation.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MovieFragment : BaseFragment() {

    private val args by navArgs<MovieFragmentArgs>()
    private val viewModel: MovieViewModel by viewModels()
    private var binding: MovieFragmentBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MovieFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
    }

    private fun setupObserver() {

        observe(viewModel.movie) {
            binding.text1.text = it?.title
            binding.text2.text = it?.overview
        }

        observe(viewModel.loadMovieEvent) {
            it.onSuccess {
                Toast.makeText(requireContext(), "Loaded movie!", Toast.LENGTH_SHORT).show()
            }
            it.onFailure { errorMessage ->
                Toast.makeText(requireContext(), "Error loading: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
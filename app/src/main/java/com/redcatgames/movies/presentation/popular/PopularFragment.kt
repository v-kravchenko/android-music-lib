package com.redcatgames.movies.presentation.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.redcatgames.movies.databinding.PopularFragmentBinding
import com.redcatgames.movies.presentation.base.BaseFragment
import com.redcatgames.movies.presentation.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PopularFragment : BaseFragment() {

    private val viewModel: PopularViewModel by viewModels()
    private var binding: PopularFragmentBinding by autoCleared()
    private val adapter by lazy { MovieAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PopularFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.text1.text = this.javaClass.simpleName
        binding.listRv.adapter = adapter

        binding.listRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                when (val layoutManager = recyclerView.layoutManager) {
                    is LinearLayoutManager -> {
                        val totalItemCount = layoutManager.itemCount
                        val lastVisible = layoutManager.findLastVisibleItemPosition()
                        val endHasBeenReached = lastVisible + 5 >= totalItemCount
                        if (totalItemCount > 0 && endHasBeenReached) {
                            viewModel.loadNextPage()
                        }
                    }
                }
            }
        })

        setupObserver()
    }

    private fun setupObserver() {

        observe(viewModel.popularMovies) {
            adapter.setItems(it)
        }

        observe(adapter.eventClickItem) {
            navigate(PopularFragmentDirections.actionPopularFragmentToMovieFragment(it.id))
        }

        observe(viewModel.loadPopularMoviesEvent) {
            it.onSuccess { movieCount ->
                Toast.makeText(requireContext(), "Loaded $movieCount movies!", Toast.LENGTH_SHORT).show()
            }
            it.onFailure { errorMessage ->
                Toast.makeText(requireContext(), "Error loading: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
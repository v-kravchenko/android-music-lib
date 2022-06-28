package com.redcatgames.movies.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.redcatgames.movies.presentation.R
import com.redcatgames.movies.presentation.base.BaseFragment
import com.redcatgames.movies.presentation.databinding.HomeFragmentBinding
import com.redcatgames.movies.presentation.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private val viewModel: HomeViewModel by viewModels()
    private var binding: HomeFragmentBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_settings -> {
                    Snackbar.make(binding.root, item.title, Snackbar.LENGTH_SHORT).show()
                    true
                }
                R.id.action_night_mode -> {
                    toggleNightMode()
                    true
                }
                else -> false
            }
        }

        binding.buttonPopular.setOnClickListener {
            navigateTo(HomeFragmentDirections.actionHomeFragmentToPopularFragment())
        }

        setupObserver()
    }

    private fun setupObserver() {}
}

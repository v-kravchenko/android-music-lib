package com.redcatgames.movies.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.redcatgames.movies.presentation.base.BaseFragment
import com.redcatgames.movies.presentation.databinding.SplashFragmentBinding
import com.redcatgames.movies.presentation.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SplashFragment : BaseFragment() {

    private val viewModel: SplashViewModel by viewModels()
    private var binding: SplashFragmentBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SplashFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()

        binding.buttonRetry.setOnClickListener { viewModel.loadDictionary() }
    }

    private fun setupObserver() {
        observe(viewModel.state) {
            Timber.d("ViewModelState is ${it.name()}")

            when (it) {
                SplashViewModel.SplashState.Loading -> {
                    binding.viewStateLoading.visibility = View.VISIBLE
                    binding.viewStateFailed.visibility = View.GONE
                }
                SplashViewModel.SplashState.Failed -> {
                    binding.viewStateFailed.visibility = View.VISIBLE
                    binding.viewStateLoading.visibility = View.GONE
                }
                SplashViewModel.SplashState.Success -> {
                    navigateTo(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
                }
            }
        }
    }
}

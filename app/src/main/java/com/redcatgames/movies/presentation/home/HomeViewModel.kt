package com.redcatgames.movies.presentation.home

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.redcatgames.movies.domain.usecase.movie.GetMovieUseCase
import com.redcatgames.movies.domain.usecase.movie.GetPopularMoviesUseCase
import com.redcatgames.movies.domain.usecase.movie.LoadMovieUseCase
import com.redcatgames.movies.domain.usecase.movie.LoadPopularMoviesUseCase
import com.redcatgames.movies.domain.util.UseCaseResult
import com.redcatgames.movies.presentation.base.BaseViewModel
import com.redcatgames.movies.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext appContext: Context,
    getPopularMoviesUseCase: GetPopularMoviesUseCase,
    getMovieUserCase: GetMovieUseCase,
    private val loadPopularMoviesUseCase: LoadPopularMoviesUseCase,
    private val loadMovieUseCase: LoadMovieUseCase
) : BaseViewModel(appContext) {

    private val moveId = 294793L

    val popularMovies = getPopularMoviesUseCase()
    val movie = getMovieUserCase(moveId)

    val loadPopularMoviesEvent = SingleLiveEvent<UseCaseResult<Int>>()

    init {
        viewModelScope.launch {
            loadPopularMoviesUseCase().run {
                loadPopularMoviesEvent.postValue(this)
            }
        }
    }
}
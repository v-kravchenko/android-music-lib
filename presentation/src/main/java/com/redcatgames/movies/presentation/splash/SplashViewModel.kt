package com.redcatgames.movies.presentation.splash

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.redcatgames.movies.domain.usecase.dictionary.LoadDictionaryUseCase
import com.redcatgames.movies.presentation.base.BaseViewModel
import com.redcatgames.movies.presentation.base.BaseViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel
@Inject
constructor(
    @ApplicationContext appContext: Context,
    private val loadDictionaryUseCase: LoadDictionaryUseCase
) : BaseViewModel(appContext) {

    sealed class SplashState : BaseViewModelState() {
        object Loading : SplashState()
        object Failed : SplashState()
        object Success : SplashState()
    }

    val state: MutableLiveData<SplashState> = MutableLiveData(SplashState.Loading)

    init {
        loadDictionary()
    }

    fun loadDictionary() =
        viewModelScope.launch {
            state.postValue(SplashState.Loading)

            loadDictionaryUseCase().run {
                state.postValue(if (isSuccess) SplashState.Success else SplashState.Failed)
            }
        }
}

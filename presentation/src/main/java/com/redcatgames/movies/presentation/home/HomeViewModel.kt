package com.redcatgames.movies.presentation.home

import android.content.Context
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.redcatgames.movies.domain.model.Language
import com.redcatgames.movies.domain.usecase.config.GetUserConfigUseCase
import com.redcatgames.movies.domain.usecase.config.SetUserConfigApiLanguageUseCase
import com.redcatgames.movies.domain.usecase.dictionary.GetLanguageUseCase
import com.redcatgames.movies.domain.usecase.dictionary.GetLanguagesUseCase
import com.redcatgames.movies.domain.usecase.dictionary.LoadDictionaryUseCase
import com.redcatgames.movies.presentation.base.BaseViewModel
import com.redcatgames.movies.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    @ApplicationContext appContext: Context,
    userConfigUseCase: GetUserConfigUseCase,
    languagesUseCase: GetLanguagesUseCase,
    languageUseCase: GetLanguageUseCase,
    private val setUserConfigApiLanguageUseCase: SetUserConfigApiLanguageUseCase,
    private val loadDictionaryUseCase: LoadDictionaryUseCase,
) : BaseViewModel(appContext) {

    val languages = languagesUseCase()
    val loadDictionaryEvent = SingleLiveEvent<Result<Unit>>()

    val language =
        Transformations.switchMap(userConfigUseCase()) { languageUseCase(it.apiLanguage) }

    init {
        loadDictionary()
    }

    private fun loadDictionary() =
        viewModelScope.launch {
            loadDictionaryUseCase().run { loadDictionaryEvent.postValue(this) }
        }

    fun setApiLanguage(language: Language) =
        viewModelScope.launch {
            setUserConfigApiLanguageUseCase(language)
            loadDictionary()
        }
}
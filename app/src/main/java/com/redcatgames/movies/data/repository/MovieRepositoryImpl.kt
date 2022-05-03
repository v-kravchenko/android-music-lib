package com.redcatgames.movies.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.redcatgames.movies.data.preferences.image.ImageConfigPreferences
import com.redcatgames.movies.data.source.local.dao.CountryDao
import com.redcatgames.movies.data.source.local.dao.MovieDao
import com.redcatgames.movies.data.source.local.mapper.mapFrom
import com.redcatgames.movies.data.source.local.mapper.mapTo
import com.redcatgames.movies.data.source.remote.NetworkService
import com.redcatgames.movies.data.source.remote.adapter.NetworkResponse
import com.redcatgames.movies.data.source.remote.mapper.mapFrom
import com.redcatgames.movies.domain.model.Country
import com.redcatgames.movies.domain.model.ImageConfig
import com.redcatgames.movies.domain.model.Movie
import com.redcatgames.movies.domain.repository.MovieRepository
import com.redcatgames.movies.domain.util.UseCaseResult
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class MovieRepositoryImpl(
    private val imageConfigPreferences: ImageConfigPreferences,
    private val countryDao: CountryDao,
    private val movieDao: MovieDao,
    private val networkService: NetworkService
) : MovieRepository {

    override suspend fun loadConfig(): UseCaseResult<Unit, String?> {
        return when (val response = networkService.getConfiguration()) {
            is NetworkResponse.Success -> {
                imageConfigPreferences.putConfig(response.body.images.mapFrom())
                UseCaseResult.Success(Unit)
            }
            is NetworkResponse.ApiError ->
                UseCaseResult.Failure(response.body.statusMessage)
            is NetworkResponse.NetworkError ->
                UseCaseResult.Failure(response.error.localizedMessage)
            is NetworkResponse.UnknownError ->
                UseCaseResult.Failure(response.error?.localizedMessage)
        }
    }

    override suspend fun loadCountries(): UseCaseResult<Unit, String?> {
        return when (val response = networkService.getCountries()) {
            is NetworkResponse.Success -> {
                deleteAllCountries()
                putCountries(response.body.map { it.mapFrom() })
                UseCaseResult.Success(Unit)
            }
            is NetworkResponse.ApiError ->
                UseCaseResult.Failure(response.body.statusMessage)
            is NetworkResponse.NetworkError ->
                UseCaseResult.Failure(response.error.localizedMessage)
            is NetworkResponse.UnknownError ->
                UseCaseResult.Failure(response.error?.localizedMessage)
        }
    }

    override suspend fun loadLanguages(): UseCaseResult<Unit, String?> {
        return when (val response = networkService.getLanguages()) {
            is NetworkResponse.Success -> {
                //TODO Save data to database
                UseCaseResult.Success(Unit)
            }
            is NetworkResponse.ApiError ->
                UseCaseResult.Failure(response.body.statusMessage)
            is NetworkResponse.NetworkError ->
                UseCaseResult.Failure(response.error.localizedMessage)
            is NetworkResponse.UnknownError ->
                UseCaseResult.Failure(response.error?.localizedMessage)
        }
    }

    override suspend fun loadPrimaryTranslations(): UseCaseResult<Unit, String?> {
        return when (val response = networkService.getPrimaryTranslations()) {
            is NetworkResponse.Success -> {
                //TODO Save data to database
                UseCaseResult.Success(Unit)
            }
            is NetworkResponse.ApiError ->
                UseCaseResult.Failure(response.body.statusMessage)
            is NetworkResponse.NetworkError ->
                UseCaseResult.Failure(response.error.localizedMessage)
            is NetworkResponse.UnknownError ->
                UseCaseResult.Failure(response.error?.localizedMessage)
        }
    }

    override suspend fun loadTimezones(): UseCaseResult<Unit, String?> {
        return when (val response = networkService.getTimezones()) {
            is NetworkResponse.Success -> {
                //TODO Save data to database
                UseCaseResult.Success(Unit)
            }
            is NetworkResponse.ApiError ->
                UseCaseResult.Failure(response.body.statusMessage)
            is NetworkResponse.NetworkError ->
                UseCaseResult.Failure(response.error.localizedMessage)
            is NetworkResponse.UnknownError ->
                UseCaseResult.Failure(response.error?.localizedMessage)
        }
    }

    override suspend fun loadMovieGenres(): UseCaseResult<Unit, String?> {
        return when (val response = networkService.getMovieGenres()) {
            is NetworkResponse.Success -> {
                //TODO Save data to database
                UseCaseResult.Success(Unit)
            }
            is NetworkResponse.ApiError ->
                UseCaseResult.Failure(response.body.statusMessage)
            is NetworkResponse.NetworkError ->
                UseCaseResult.Failure(response.error.localizedMessage)
            is NetworkResponse.UnknownError ->
                UseCaseResult.Failure(response.error?.localizedMessage)
        }
    }

    override suspend fun loadDictionary(): UseCaseResult<Unit, String?> {
        val res = coroutineScope {
            val configResult = async { loadConfig() }
            val countriesResult = async { loadCountries() }
            val languagesResult = async { loadLanguages() }
            val primaryTranslationsResult = async { loadPrimaryTranslations() }
            val timezonesResult = async { loadTimezones() }
            val genreMovieResult = async { loadMovieGenres() }
            val jobList = awaitAll(
                configResult,
                countriesResult,
                languagesResult,
                primaryTranslationsResult,
                timezonesResult,
                genreMovieResult
            )

            jobList.find { it.isFailure }?.let {
                if (it is UseCaseResult.Failure) {
                    return@coroutineScope UseCaseResult.Failure(it.error)
                }
            }

            return@coroutineScope UseCaseResult.Success<Unit, String?>(Unit)
        }

        return res
    }

    override suspend fun putCountries(countries: List<Country>) {
        countryDao.insertAll(countries.map { it.mapTo() })
    }

    override suspend fun putMovies(movies: List<Movie>) {
        movieDao.insertAll(movies.map { it.mapTo() })
    }

    override suspend fun deleteAllCountries(): UseCaseResult<Int, Unit> {
        val rowCount = countryDao.getCount()
        countryDao.deleteAll()
        return UseCaseResult.Success(rowCount)
    }

    override suspend fun deleteAllMovies(): UseCaseResult<Int, Unit> {
        val rowCount = movieDao.getCount()
        movieDao.deleteAll()
        return UseCaseResult.Success(rowCount)
    }

    override fun imageConfig(): LiveData<ImageConfig> =
        imageConfigPreferences.imageConfig

    override suspend fun putMovie(movie: Movie) {
        movieDao.insert(movie.mapTo())
    }

    override suspend fun loadMovie(movieId: Long): UseCaseResult<Unit, String?> {

        return when (val response = networkService.getMovie(movieId)) {
            is NetworkResponse.Success -> {
                putMovie(response.body.mapFrom())
                UseCaseResult.Success(Unit)
            }
            is NetworkResponse.ApiError ->
                UseCaseResult.Failure(response.body.statusMessage)
            is NetworkResponse.NetworkError ->
                UseCaseResult.Failure(response.error.localizedMessage)
            is NetworkResponse.UnknownError ->
                UseCaseResult.Failure(response.error?.localizedMessage)
        }
    }

    override suspend fun loadPopularMovies(page: Int): UseCaseResult<List<Movie>, String?> {

        return when (val response = networkService.getPopularMovies(page)) {
            is NetworkResponse.Success -> {
                val movieList = response.body.movies.map { it.mapFrom() }
                deleteAllMovies()
                putMovies(movieList)
                UseCaseResult.Success(movieList)
            }
            is NetworkResponse.ApiError ->
                UseCaseResult.Failure(response.body.statusMessage)
            is NetworkResponse.NetworkError ->
                UseCaseResult.Failure(response.error.localizedMessage)
            is NetworkResponse.UnknownError ->
                UseCaseResult.Failure(response.error?.localizedMessage)
        }
    }

    override fun popularMovies(): LiveData<List<Movie>> {
        return Transformations.map(movieDao.getPopular()) {
            it.map { movieEntity -> movieEntity.mapFrom() }
        }
    }

    override fun movie(movieId: Long): LiveData<Movie?> {
        return Transformations.map(movieDao.getById(movieId)) {
            it?.mapFrom()
        }
    }
}
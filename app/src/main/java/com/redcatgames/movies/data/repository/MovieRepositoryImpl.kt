package com.redcatgames.movies.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asFlow
import com.redcatgames.movies.data.source.local.dao.GenreDao
import com.redcatgames.movies.data.source.local.dao.MovieDao
import com.redcatgames.movies.data.source.local.dao.MovieGenreDao
import com.redcatgames.movies.data.source.local.mapper.mapFrom
import com.redcatgames.movies.data.source.local.mapper.mapTo
import com.redcatgames.movies.data.source.remote.NetworkService
import com.redcatgames.movies.data.source.remote.adapter.NetworkResponse
import com.redcatgames.movies.data.source.remote.mapper.mapFrom
import com.redcatgames.movies.domain.model.Movie
import com.redcatgames.movies.domain.model.MovieGenre
import com.redcatgames.movies.domain.model.MovieInfo
import com.redcatgames.movies.domain.repository.MovieRepository
import com.redcatgames.movies.domain.util.UseCaseResult
import com.redcatgames.movies.util.empty
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class MovieRepositoryImpl(
    private val movieDao: MovieDao,
    private val movieGenreDao: MovieGenreDao,
    private val genreDao: GenreDao,
    private val networkService: NetworkService
) : MovieRepository {

    override suspend fun deleteAllMovies(): UseCaseResult<Int, Unit> {
        val rowCount = movieDao.getCount()
        movieDao.deleteAll()
        return UseCaseResult.Success(rowCount)
    }

    override suspend fun putMovies(movies: List<Movie>) {
        movieDao.insertAll(movies.map { it.mapTo() })
    }

    private suspend fun putMovieGenres(movie: Movie, genres: List<MovieGenre>) {
        val localGenres = movieGenres(movie.id).asFlow().first()
        if (localGenres.sortedBy { it.genreId } != genres.sortedBy { it.genreId }) {
            movieGenreDao.deleteByMovie(movie.id)
            movieGenreDao.insertAll(genres.map { it.mapTo() })
        }
    }

    private suspend fun putMoviesGenres(movies: List<Movie>, moviesGenres: MutableList<MovieGenre>) {
        movieGenreDao.deleteByMovieList(movies.map { it.id })
        movieGenreDao.insertAll(moviesGenres.map { it.mapTo() })
    }

    override suspend fun putMovie(movie: Movie) {
        if (movie != movie(movie.id).asFlow().firstOrNull()) {
            movieDao.insert(movie.mapTo())
        }
    }

    override suspend fun loadMovie(movieId: Long): UseCaseResult<Unit, String?> {

        return when (val response = networkService.getMovie(movieId)) {
            is NetworkResponse.Success -> {
                val movie = response.body.mapFrom()
                val genres = response.body.genres.map { it.mapFrom(movie) }
                putMovie(movie)
                putMovieGenres(movie, genres)
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

                val movies = response.body.movies.map { it.mapFrom() }
                deleteAllMovies()
                putMovies(movies)

                val genreList = genreDao.loadAll()
                val moveGenreList = mutableListOf<MovieGenre>()
                response.body.movies.forEach { movie ->
                    moveGenreList.addAll(movie.genreIds.map {
                        MovieGenre(
                            movieId = movie.id,
                            genreId = it,
                            genreName = genreList.find { genre -> genre.id == it }?.name ?: String.empty
                        ) })
                }
                putMoviesGenres(movies, moveGenreList)

                UseCaseResult.Success(movies)
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

    override fun movieInfo(movieId: Long): LiveData<MovieInfo?> {
        return Transformations.map(movieDao.getInfoById(movieId)) {
            it?.mapFrom()
        }
    }

    override fun movieGenres(movieId: Long): LiveData<List<MovieGenre>> {
        return Transformations.map(movieGenreDao.getByMovie(movieId)) {
            it?.map { movieGenreEntity -> movieGenreEntity.mapFrom() }
        }
    }
}
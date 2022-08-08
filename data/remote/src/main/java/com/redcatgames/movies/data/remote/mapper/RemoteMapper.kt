package com.redcatgames.movies.data.remote.mapper

import com.redcatgames.movies.data.remote.response.configuration.ConfigurationCountriesResult
import com.redcatgames.movies.data.remote.response.configuration.ConfigurationLanguagesResult
import com.redcatgames.movies.data.remote.response.configuration.ConfigurationResult
import com.redcatgames.movies.data.remote.response.configuration.ConfigurationTimezonesResult
import com.redcatgames.movies.data.remote.response.discover.movie.DiscoverMovieResult
import com.redcatgames.movies.data.remote.response.movie.GenreResult
import com.redcatgames.movies.data.remote.response.movie.MovieResult
import com.redcatgames.movies.data.remote.response.movie.credits.MovieCreditsResult
import com.redcatgames.movies.data.remote.response.person.PersonResult
import com.redcatgames.movies.data.remote.response.person.credits.PersonCreditsResult
import com.redcatgames.movies.domain.model.*
import com.redcatgames.movies.util.empty

fun ConfigurationCountriesResult.toCountry() =
    Country(iso ?: String.empty, englishName ?: String.empty, nativeName ?: String.empty)

fun ConfigurationLanguagesResult.toLanguage() =
    Language(iso ?: String.empty, englishName ?: String.empty, name ?: String.empty)

fun ConfigurationTimezonesResult.toTimezoneList(): List<Timezone> {
    return mutableListOf<Timezone>()
        .apply { addAll(zones.map { Timezone(iso ?: String.empty, it) }) }
        .toList()
}

fun GenreResult.Genre.toGenre() = Genre(id, name ?: String.empty)

fun ConfigurationResult.Images.toImageConfig() =
    ImageConfig(
        baseUrl = baseUrl ?: String.empty,
        secureBaseUrl = secureBaseUrl ?: String.empty,
        backdropSizes = backdropSizes,
        logoSizes = logoSizes,
        posterSizes = posterSizes,
        profileSizes = profileSizes,
        stillSizes = stillSizes
    )

fun DiscoverMovieResult.Movie.toMovie() =
    Movie(
        id = id,
        isAdult = isAdult,
        backdropPath = backdropPath,
        genreIds = genreIds,
        originalLanguage = originalLanguage ?: String.empty,
        originalTitle = originalTitle ?: String.empty,
        overview = overview ?: String.empty,
        popularity = popularity,
        posterPath = posterPath ?: String.empty,
        releaseDate = releaseDate,
        title = title ?: String.empty,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount
    )

fun MovieResult.toMovie() =
    Movie(
        id = id,
        isAdult = isAdult,
        backdropPath = backdropPath,
        genreIds = genres.map { it.id },
        originalLanguage = originalLanguage ?: String.empty,
        originalTitle = originalTitle ?: String.empty,
        overview = overview ?: String.empty,
        popularity = popularity,
        posterPath = posterPath ?: String.empty,
        releaseDate = releaseDate,
        title = title ?: String.empty,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount
    )

fun MovieResult.Genre.toMovieGenre(movie: Movie) =
    MovieGenre(movieId = movie.id, genreId = id, genreName = name ?: String.empty)

fun PersonCreditsResult.toPersonCastList(): List<PersonCast> {
    return mutableListOf<PersonCast>()
        .apply {
            addAll(
                castList.map {
                    PersonCast(
                        creditId = it.creditId,
                        personId = this@toPersonCastList.personId,
                        isAdult = it.isAdult,
                        backdropPath = it.backdropPath,
                        genreIds = it.genreIds,
                        movieId = it.movieId,
                        originalLanguage = it.originalLanguage,
                        originalTitle = it.originalTitle,
                        overview = it.overview,
                        popularity = it.popularity,
                        posterPath = it.posterPath,
                        releaseDate = it.releaseDate,
                        title = it.title,
                        isVideo = it.isVideo,
                        voteAverage = it.voteAverage,
                        voteCount = it.voteCount,
                        character = it.character,
                        order = it.order
                    )
                }
            )
        }
}

fun MovieCreditsResult.toMovieCastList(): List<MovieCast> {
    return mutableListOf<MovieCast>()
        .apply {
            addAll(
                castList.map {
                    MovieCast(
                        personId = it.personId,
                        movieId = this@toMovieCastList.movieId,
                        adult = it.isAdult,
                        gender = it.gender,
                        knownForDepartment = it.knownForDepartment,
                        name = it.name ?: String.empty,
                        originalName = it.originalName ?: String.empty,
                        popularity = it.popularity,
                        profilePath = it.profilePath,
                        castId = it.castId,
                        character = it.character ?: String.empty,
                        creditId = it.creditId,
                        order = it.order
                    )
                }
            )
        }
        .toList()
}

fun PersonCreditsResult.toPersonCrewList(): List<PersonCrew> {
    return mutableListOf<PersonCrew>()
        .apply {
            addAll(
                crewList.map {
                    PersonCrew(
                        creditId = it.creditId,
                        personId = this@toPersonCrewList.personId,
                        isAdult = it.isAdult,
                        backdropPath = it.backdropPath,
                        genreIds = it.genreIds,
                        movieId = it.movieId,
                        originalLanguage = it.originalLanguage,
                        originalTitle = it.originalTitle,
                        overview = it.overview,
                        popularity = it.popularity,
                        posterPath = it.posterPath,
                        releaseDate = it.releaseDate,
                        title = it.title,
                        isVideo = it.isVideo,
                        voteAverage = it.voteAverage,
                        voteCount = it.voteCount,
                        department = it.department,
                        job = it.job
                    )
                }
            )
        }
}

fun MovieCreditsResult.toMovieCrewList(): List<MovieCrew> {
    return mutableListOf<MovieCrew>()
        .apply {
            addAll(
                crewList.map {
                    MovieCrew(
                        personId = it.personId,
                        movieId = this@toMovieCrewList.movieId,
                        adult = it.isAdult,
                        gender = it.gender,
                        knownForDepartment = it.knownForDepartment,
                        name = it.name ?: String.empty,
                        originalName = it.originalName ?: String.empty,
                        popularity = it.popularity,
                        profilePath = it.profilePath,
                        creditId = it.creditId,
                        department = it.department ?: String.empty,
                        job = it.job
                    )
                }
            )
        }
        .toList()
}

fun PersonResult.toPerson() =
    Person(
        id,
        isAdult,
        alsoKnownAs,
        biography,
        birthDay,
        deathDay,
        gender,
        homepage,
        imdbId,
        knownForDepartment,
        name,
        placeOfBirth,
        popularity,
        profilePath
    )

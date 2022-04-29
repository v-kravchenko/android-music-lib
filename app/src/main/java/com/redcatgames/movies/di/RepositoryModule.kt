package com.redcatgames.movies.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.redcatgames.movies.data.repository.ArtistRepositoryImpl
import com.redcatgames.movies.data.source.local.AppDatabase
import com.redcatgames.movies.data.source.local.dao.ArtistDao
import com.redcatgames.movies.domain.repository.ArtistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideArtistRepository(artistDao: ArtistDao): ArtistRepository {
        return ArtistRepositoryImpl(artistDao)
    }

}

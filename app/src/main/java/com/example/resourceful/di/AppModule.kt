package com.example.resourceful.di

import android.app.Application
import androidx.room.Room
import com.example.resourceful.data.local.ResourceDatabase
import com.example.resourceful.data.repository.ResourceRepositoryImpl
import com.example.resourceful.domain.repository.ResourceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideResourceDatabase(app: Application): ResourceDatabase {
        return Room.databaseBuilder(
            app,
            ResourceDatabase::class.java,
            ResourceDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideResourceRepository(db: ResourceDatabase): ResourceRepository {
        return ResourceRepositoryImpl(db.resourceDao)
    }
}
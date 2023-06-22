package com.codefal.mystoryapp.network

import android.content.Context
import androidx.room.Room
import com.codefal.mystoryapp.database.DatabaseStory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseConfig {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    )= Room.databaseBuilder(
        context, DatabaseStory::class.java, "story"
    ).build()
}
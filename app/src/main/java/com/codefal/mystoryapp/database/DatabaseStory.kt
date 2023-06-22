package com.codefal.mystoryapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codefal.mystoryapp.network.model.ListStoryItem
import com.codefal.mystoryapp.network.model.RemoteKeys

@Database(
    entities = [ListStoryItem::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseStory : RoomDatabase() {
    abstract fun daoStory(): DaoStory
    abstract fun remoteStory(): RemoteStory
}
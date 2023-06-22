package com.codefal.mystoryapp.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codefal.mystoryapp.network.model.ListStoryItem

@Dao
interface DaoStory{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story : List<ListStoryItem>)

    @Query("SELECT * FROM table_story")
    fun getAllStory(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM table_story")
    suspend fun deleteAllStory()
}
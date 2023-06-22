package com.codefal.mystoryapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.codefal.mystoryapp.data.RemoteMediatorStory
import com.codefal.mystoryapp.database.DatabaseStory
import com.codefal.mystoryapp.network.model.ListStoryItem
import javax.inject.Inject

class PagingRepository @Inject constructor(private val databaseStory: DatabaseStory, private val repository: StoryRepository) {

    fun getStory(token: String): LiveData<PagingData<ListStoryItem>>{
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = RemoteMediatorStory(token, repository, databaseStory),
            pagingSourceFactory = {
                databaseStory.daoStory().getAllStory()
            }
        ).liveData
    }
}
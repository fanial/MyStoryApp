package com.codefal.mystoryapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.codefal.mystoryapp.database.DatabaseStory
import com.codefal.mystoryapp.network.model.ListStoryItem
import com.codefal.mystoryapp.network.model.RemoteKeys
import com.codefal.mystoryapp.repository.StoryRepository
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class RemoteMediatorStory @Inject constructor(
    private val token : String,
    private val repository: StoryRepository,
    private val database: DatabaseStory
    ): RemoteMediator<Int, ListStoryItem>(){

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryItem>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH ->{
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        try {
            val responseData = repository.getStory(token, page, state.config.pageSize)
            val data = responseData.body()?.listStory
            val endOfPaginationReacted = data.isNullOrEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH){
                    with(database){
                        remoteStory().deleteRemoteKeys()
                        daoStory().deleteAllStory()
                    }
                }
                val prefKeys = if (page == 1) null else page -1
                val nextKeys = if (endOfPaginationReacted) null else page +1
                val keys = data?.map {
                    RemoteKeys(id = it.id, prevKey = prefKeys, nextKey = nextKeys)
                }
                database.remoteStory().insertAll(keys!!)
                database.daoStory().insertStory(data)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReacted)
        }catch (exception: Exception){
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            data.id.let { database.remoteStory().getRemoteKeys(it) }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            data.id.let { database.remoteStory().getRemoteKeys(it) }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteStory().getRemoteKeys(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}
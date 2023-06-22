package com.codefal.mystoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.codefal.mystoryapp.network.model.ListStoryItem
import com.codefal.mystoryapp.repository.PagingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PagingViewModel @Inject constructor(private val repository: PagingRepository): ViewModel() {

    fun story(token: String ): LiveData<PagingData<ListStoryItem>> = repository.getStory(token).cachedIn(viewModelScope)
}
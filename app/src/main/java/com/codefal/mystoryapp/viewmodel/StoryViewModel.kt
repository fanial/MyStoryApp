package com.codefal.mystoryapp.viewmodel

import androidx.lifecycle.ViewModel
import com.codefal.mystoryapp.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(private val storyRepo: StoryRepository): ViewModel() {

    fun getDetail(token: String, storyId: String) = storyRepo.getDetail(token, storyId)
    fun addStory(token: String, desc: RequestBody, photo: MultipartBody.Part) = storyRepo.addStory(token, desc, photo)
    fun getLocStory(token: String) = storyRepo.getLocStory(token)
    fun messageObserver() = storyRepo.messageObserver()
    fun loadingObserver() = storyRepo.isLoading()
    fun detailStoryObserver() = storyRepo.detailStoryObserver()
    fun mapsObserver() = storyRepo.mapsObserver()
}
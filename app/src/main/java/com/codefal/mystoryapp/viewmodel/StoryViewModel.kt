package com.codefal.mystoryapp.viewmodel

import androidx.lifecycle.ViewModel
import com.codefal.mystoryapp.viewmodel.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(private val storyRepo: StoryRepository): ViewModel() {

    fun getStory(token : String) = storyRepo.getListStory(token)
    fun getDetail(token: String, storyId: String) = storyRepo.getDetail(token, storyId)
    fun addStory(token: String, desc: RequestBody, photo: MultipartBody.Part) = storyRepo.addStory(token, desc, photo)
    fun messageObserver() = storyRepo.messageObserver()
    fun loadingObserver() = storyRepo.isLoading()
    fun listStoryObserver() = storyRepo.listStoryObserver()
    fun detailStoryObserver() = storyRepo.detailStoryObserver()

}
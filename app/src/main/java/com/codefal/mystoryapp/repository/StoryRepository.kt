package com.codefal.mystoryapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codefal.mystoryapp.network.ApiService
import com.codefal.mystoryapp.network.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class StoryRepository @Inject constructor(private val api: ApiService) {

    private val detailStory = MutableLiveData<Story?>()
    fun detailStoryObserver() : MutableLiveData<Story?> = detailStory

    private val addStory = MutableLiveData<ResponseErrorMessage?>()

    private val listMaps = MutableLiveData<List<ListStoryItem?>?>()
    fun mapsObserver() : LiveData<List<ListStoryItem?>?> = listMaps

    private val message : MutableLiveData<String?> = MutableLiveData()
    fun messageObserver(): LiveData<String?> = message

    private val _loading = MutableLiveData<Boolean>()
    fun isLoading(): LiveData<Boolean> = _loading

    suspend fun getStory(token: String, page: Int, size: Int) = withContext(Dispatchers.IO){
        api.getStories(token, page, size)
    }

    fun getDetail(token: String, idStory: String){
        _loading.value = true
        api.getDetailStories(token, idStory).enqueue(object : Callback<ResponseDetailStory>{
            override fun onResponse(
                call: Call<ResponseDetailStory>,
                response: Response<ResponseDetailStory>
            ) {
                _loading.value = false
                val body = response.body()
                if (response.isSuccessful){
                    if (body != null){
                        detailStory.postValue(body.story)
                        message.value = body.message
                        Log.i("Success", "onResponse: Success Load Story")
                    }else{
                        detailStory.value = null
                        val error = response.message()
                        message.value = error
                        Log.e("Failed", "onResponse: Failed Load Story Null")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDetailStory>, t: Throwable) {
                message.value= t.message
                _loading.value = false
                Log.e("Response Error", "onFailure: ${t.message}")
            }

        })

    }

    fun addStory(token: String, desc: RequestBody, photo: MultipartBody.Part){
        _loading.value = true
        api.addStories(token, desc, photo).enqueue(object : Callback<ResponseErrorMessage>{
            override fun onResponse(
                call: Call<ResponseErrorMessage>,
                response: Response<ResponseErrorMessage>
            ) {
                _loading.value = false
                if (response.isSuccessful){
                    val body = response.body()
                    if (body != null){
                        addStory.postValue(body)
                        message.value = body.message
                        Log.i("Success", "onResponse: Success Add Story")
                    }else{
                        addStory.postValue(null)
                        val error = response.message()
                        message.value = error
                        Log.e("Fail", "onResponse: Failed Add Story Null")
                    }
                }else{
                    addStory.postValue(null)
                    val error = response.message()
                    message.value = error
                    Log.e("Failed", "onResponse: Failed Add Story Null")
                }
            }

            override fun onFailure(call: Call<ResponseErrorMessage>, t: Throwable) {
                addStory.postValue(null)
                message.value= t.message
                _loading.value = false
                Log.e("Response Error", "onFailure: ${t.message}")
            }

        })
    }

    fun getLocStory(token: String){
        _loading.value = true
        api.getLocStory(token, 1).enqueue(object : Callback<ResponseStories>{
            override fun onResponse(
                call: Call<ResponseStories>,
                response: Response<ResponseStories>
            ) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body != null){
                        listMaps.postValue(body.listStory)
                        message.value = body.message
                        Log.i("Success", "${body.listStory}")
                    }else{
                        listMaps.value = null
                        val error = response.message()
                        message.value = error
                        Log.e("Fail", "onResponse: Failed Get Story Null")
                    }
                }else{
                    listMaps.value = null
                    val error = response.message()
                    message.value = error
                    Log.e("Failed", "onResponse: Get Story Null")
                }
            }

            override fun onFailure(call: Call<ResponseStories>, t: Throwable) {
                listMaps.value = null
                message.value= t.message
                _loading.value = false
                Log.e("Response Error", "onFailure: ${t.message}", t)
            }

        })
    }
}
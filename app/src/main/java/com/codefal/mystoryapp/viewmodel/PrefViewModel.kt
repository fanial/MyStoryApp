package com.codefal.mystoryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.codefal.mystoryapp.viewmodel.repository.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrefViewModel @Inject constructor(private val store: DataStoreManager): ViewModel() {
    fun setToken(token: String, name: String, status: Boolean){
        viewModelScope.launch {
            store.saveToken(token, name, status)
        }
    }

    fun getToken() = store.getToken().asLiveData()

    fun getName()= store.getName().asLiveData()

    fun getStatus()= store.getStatus().asLiveData()
    fun logout(){
        viewModelScope.launch { store.delete() }
    }

}
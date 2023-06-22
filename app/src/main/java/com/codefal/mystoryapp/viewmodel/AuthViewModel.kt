package com.codefal.mystoryapp.viewmodel

import androidx.lifecycle.ViewModel
import com.codefal.mystoryapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel(){

    fun doLogin(email: String, password: String) = authRepository.doLogin(email, password)

    fun doRegister(name:String, email: String, password: String) = authRepository.doRegister(name, email, password)

    fun messageObserver() = authRepository.messageObserver()
    fun doLoginObserver() = authRepository.doLoginObserver()
    fun doRegisterObserver() = authRepository.doRegisterObserver()
    fun loadingObserver() = authRepository.isLoading()
}
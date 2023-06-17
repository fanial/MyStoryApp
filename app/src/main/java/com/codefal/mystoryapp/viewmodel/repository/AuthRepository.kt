package com.codefal.mystoryapp.viewmodel.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codefal.mystoryapp.model.ResponseErrorMessage
import com.codefal.mystoryapp.model.ResponseLogin
import com.codefal.mystoryapp.network.ApiService
import com.codefal.mystoryapp.utils.Validation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api: ApiService){

    private val message : MutableLiveData<String?> = MutableLiveData()
    fun messageObserver(): LiveData<String?> = message

    private val _loading = MutableLiveData<Boolean>()
    fun isLoading(): LiveData<Boolean> = _loading

    private val doLogin : MutableLiveData<ResponseLogin?> = MutableLiveData()
    fun doLoginObserver() : LiveData<ResponseLogin?> = doLogin

    private val doRegister : MutableLiveData<ResponseErrorMessage?> = MutableLiveData()
    fun doRegisterObserver() : LiveData<ResponseErrorMessage?> = doRegister

    fun doLogin(email: String, password: String){
        _loading.value = true
        api.login(email, password).enqueue(object : Callback<ResponseLogin>{
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                _loading.value = false
                val body = response.body()
                if (response.isSuccessful){
                    if (body != null) {
                        doLogin.postValue(body)
                        message.postValue("Halo ${body.loginResult?.name}")
                        Log.d("Success", "onResponse: Login ${body.loginResult?.name} ")
                    }
                }else{
                    doLogin.value = null
                    val error = Validation.errorValidation(response.code())
                    message.value = error
                    Log.e("Failed Response", "onResponse: Login ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                doLogin.postValue(null)
                message.value= t.message
                _loading.value = false
                Log.e("Response Error", "onFailure: ${t.message}")
            }

        })
    }

    fun doRegister(name : String, email: String, password: String){
        _loading.value = true
        api.register(name, email, password).enqueue(object : Callback<ResponseErrorMessage>{
            override fun onResponse(
                call: Call<ResponseErrorMessage>,
                response: Response<ResponseErrorMessage>
            ) {
                _loading.value = false
                val body = response.body()
                if (response.isSuccessful){
                    if (body != null){
                        doRegister.postValue(body)
                        message.postValue(body.message)
                        Log.d("Success", "onResponse: $body")
                    }
                }else{
                    doRegister.postValue(null)
                    val error = Validation.errorValidation(response.code())
                    message.value = error
                    Log.e("Failed", "onResponse: Register ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseErrorMessage>, t: Throwable) {
                doRegister.postValue(null)
                message.postValue(t.message)
                _loading.value = false
                Log.e("Response Error", "onFailure: ${t.message}")
            }

        })
    }


}
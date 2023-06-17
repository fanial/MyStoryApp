package com.codefal.mystoryapp.utils

object Validation {

    fun errorValidation(code : Int): String{
        var error = ""
        error = if(code == 401){
            "Wrong Username or Password"
        }else{
            "User not found"
        }
        return error
    }
}
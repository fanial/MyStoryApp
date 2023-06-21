package com.codefal.mystoryapp.network

import com.codefal.mystoryapp.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") pass: String
    ) : Call<ResponseErrorMessage>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") pass: String
    ) : Call<ResponseLogin>

    @POST("stories")
    @Multipart
    fun addStories(
        @Header("Authorization") token : String,
        @Part("description") desc : RequestBody,
        @Part photo : MultipartBody.Part,
    ) : Call<ResponseErrorMessage>

    @GET("stories")
    fun getStories(
        @Header("Authorization") token : String,
    ) : Call<ResponseStories>

    @GET("stories/{id}")
    fun getDetailStories(
        @Header("Authorization") token : String,
        @Path("id") storyId: String
    ) : Call<ResponseDetailStory>

    @GET("stories")
    fun getLocStory(
        @Header("Authorization") token: String,
        @Query("location") loc : Int? = null
    ) : Call<ResponseStories>
}
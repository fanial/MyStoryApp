package com.codefal.mystoryapp.network

import com.codefal.mystoryapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiConfig {
    private const val BASE_URL = "https://story-api.dicoding.dev/v1/"

    private val loggingInterceptor = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    private val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    @Singleton
    @Provides
    fun instance(): Retrofit =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()

    @Singleton
    @Provides
    fun endPoint(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

}
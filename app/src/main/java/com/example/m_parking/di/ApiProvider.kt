package com.example.m_parking.di

import android.content.Context
import com.example.m_parking.data.api.ParkingApi
import com.example.m_parking.data.api.UserApi
import com.example.m_parking.data.repository.AuthTokenStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object ApiProvider {
    private const val BASE_URL = "https://f014-41-111-189-173.ngrok-free.app"

    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val tokenManager = AuthTokenStoreRepository(context)
        val accessTokenInterceptor = AccessTokenInterceptor(tokenManager)
        return OkHttpClient.Builder()
            .addInterceptor(accessTokenInterceptor)
            .build()
    }

    @Provides
    fun provideRetrofit(@ApplicationContext context: Context): Retrofit {
        val okHttpClient = provideOkHttpClient(context)
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideUserApiService(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    fun provideParkingApiService(retrofit: Retrofit): ParkingApi {
        return retrofit.create(ParkingApi::class.java)
    }
}
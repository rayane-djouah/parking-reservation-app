package com.example.m_parking.di

import com.example.m_parking.data.repository.AuthTokenStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Response

class AccessTokenInterceptor(private val tokenRepository: AuthTokenStoreRepository) : Interceptor {
    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val TOKEN_TYPE = "Bearer"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return runBlocking {
            val token = getToken()
            if (token.isNotBlank()) {
                val newRequest = request.newBuilder()
                    .addHeader(HEADER_AUTHORIZATION, "$TOKEN_TYPE $token")
                    .build()
                chain.proceed(newRequest)
            } else {
                chain.proceed(request)
            }
        }
    }

    private suspend fun getToken(): String {
        return withContext(Dispatchers.IO) {
            tokenRepository.readTokenState().first() // Collect the first value emitted by the flow
        }
    }
}

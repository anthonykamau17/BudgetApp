package com.anthonykamau.budgetapp.util

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class ClaudeMessage(
    val role: String,
    val content: String
)

data class ClaudeRequest(
    val model: String,
    val max_tokens: Int,
    val system: String? = null, // Added system parameter
    val messages: List<ClaudeMessage>
)

data class ClaudeContent(
    val type: String,
    val text: String
)

data class ClaudeResponse(
    val content: List<ClaudeContent>
)

interface ClaudeApiService {
    @Headers(
        "Content-Type: application/json",
        "anthropic-version: 2023-06-01"
    )
    @POST("v1/messages")
    suspend fun sendMessage(
        @retrofit2.http.Header("x-api-key") apiKey: String,
        @Body request: ClaudeRequest
    ): ClaudeResponse

    companion object {
        private const val BASE_URL = "https://api.anthropic.com/"

        fun create(): ClaudeApiService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ClaudeApiService::class.java)
        }
    }
}
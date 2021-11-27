package com.example.newsapi.api

import com.example.newsapi.model.TopHeadLineResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("/v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String = "871cfa9beeca4f688b195ed3f5861024"
    ): Response<TopHeadLineResponse>
}
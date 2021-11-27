package com.example.newsapi.model

data class TopHeadLineResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)
package com.example.newsapi.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapi.model.Article
import com.example.newsapi.model.TopHeadLineResponse
import com.example.newsapi.repository.HeadlinesRepository
import com.example.newsapi.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: HeadlinesRepository) : ViewModel() {


  private  val breakingNews:MutableLiveData<Resource<TopHeadLineResponse>> = MutableLiveData()
    var breakingNewsPageNumber=1

    private  val searchNews:MutableLiveData<Resource<TopHeadLineResponse>> = MutableLiveData()
    var searchNewsPageNumber=1
    var breakingNewsResponse:TopHeadLineResponse? =null

    init {
        getbreakingNews("in")
    }



    private fun getbreakingNews(country:String) =viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response=repository.getBreakingNews(country,breakingNewsPageNumber)
        breakingNews.postValue(handleBreakingNews(response))
    }

    fun searchNews(searchQuery:String)=viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response=repository.searchNews(searchQuery,searchNewsPageNumber)
        searchNews.postValue(handleBreakingNews(response))

    }

    private  fun handleBreakingNews(response:Response<TopHeadLineResponse>):Resource<TopHeadLineResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse->

                breakingNewsPageNumber++
                if(breakingNewsResponse==null){
                    breakingNewsResponse=resultResponse

                }else{
                    val oldArticles= breakingNewsResponse?.articles
                    val newArticles=resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }

                return  Resource.Success(breakingNewsResponse?:resultResponse)
            }
        }
        return  Resource.Error(response.message())
    }

    fun saveArticle(article: Article)=viewModelScope.launch {
        repository.upsert(article)
    }
    fun getSavedNews():LiveData<List<Article>> = repository.getSavedNews()
    fun deleteArticle(article: Article)=viewModelScope.launch {
        repository.deleteArticle(article)
    }



    val headlineResponse: LiveData<TopHeadLineResponse>
        get() = repository.headlines
      val bNews:LiveData<Resource<TopHeadLineResponse>>
          get() =breakingNews
    val searchnews: LiveData<Resource<TopHeadLineResponse>>
    get() = searchNews



}
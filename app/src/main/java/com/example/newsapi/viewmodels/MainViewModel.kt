package com.example.newsapi.viewmodels

import android.icu.util.ULocale.getCountry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapi.model.TopHeadLineResponse
import com.example.newsapi.repository.HeadlinesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private  val repository: HeadlinesRepository):ViewModel() {
  private   var country:String
  private val  showAdds:Boolean
    init {
        country= repository.getUserCountry()!!
        showAdds=repository.showAdds()
        viewModelScope.launch(Dispatchers.IO) {
            repository.getHeadlineRespose(country)
        }

    }


    val getcountry:String
        get()=country
    val headlineResponse:LiveData<TopHeadLineResponse>
        get() =repository.headlines
    val showadd:Boolean
        get() =showAdds


}
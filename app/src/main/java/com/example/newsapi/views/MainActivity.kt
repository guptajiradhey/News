package com.example.newsapi.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.NewsApplication
import com.example.newsapi.R
import com.example.newsapi.adapters.HeadineAdapter
import com.example.newsapi.viewmodels.MainViewModel
import com.example.newsapi.viewmodels.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    lateinit var adapter: HeadineAdapter

    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager = GridLayoutManager(this, 2)


        val headlinesRepository = (application as NewsApplication).repository

        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(headlinesRepository)
        ).get(MainViewModel::class.java)
        Toast.makeText(this,"You are in "+mainViewModel.getcountry+"country",Toast.LENGTH_LONG).show()

        mainViewModel.headlineResponse.observe(this, {

            var list = it.articles
            adapter = HeadineAdapter(list,this)
            recyclerView.adapter = adapter

        })
        
        Toast.makeText(this,mainViewModel.showadd.toString(),Toast.LENGTH_LONG).show()
    }
}
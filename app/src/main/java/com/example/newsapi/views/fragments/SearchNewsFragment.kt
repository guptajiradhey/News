package com.example.newsapi.views.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.R
import com.example.newsapi.adapters.HeadineAdapter
import com.example.newsapi.db.ArticleDatabase
import com.example.newsapi.repository.HeadlinesRepository
import com.example.newsapi.util.Resource
import com.example.newsapi.viewmodels.MainViewModel
import com.example.newsapi.viewmodels.MainViewModelFactory
import com.example.newsapi.views.MainActivity
import com.example.newsapi.views.WebViewActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment:Fragment(R.layout.fragment_search_news){
    lateinit var  mainViewModel: MainViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: HeadineAdapter
    lateinit var  progressBar: ProgressBar
    lateinit var  etSearch:EditText
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvSearchNews)
        progressBar = view.findViewById(R.id.paginationProgressBar)
        etSearch = view.findViewById(R.id.etSearch)

        setUpRecyclerView()
        setUpViewModel()


        adapter.setOnItenClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
//            val intent= Intent(activity, WebViewActivity::class.java)
//            intent.putExtra("url",it.url)
////            intent.putExtras(bundle)
//            activity?.startActivity(intent)

        }


           var job:Job?=null
        etSearch.addTextChangedListener {  editable ->
            job?.cancel()
            job= MainScope().launch {
                delay(500)
                editable.let {
                    if(editable.toString().isNotEmpty()){
                        mainViewModel.searchNews(editable.toString())
                    }
                }
            }


        }

        mainViewModel.searchnews.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success->{
                    hideProgreesBar()
                    response.data?.let { newsResponse->
                        adapter.differ.submitList(newsResponse.articles)

                    }
                }

                is Resource.Error->{
                    hideProgreesBar()
                    response.message.let {
                        Toast.makeText(activity,it, Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading->{
                    showProgreeBar()
                }

            }

        })
    }


    private fun showProgreeBar() {
        progressBar.visibility=View.VISIBLE
    }

    private fun hideProgreesBar() {
        progressBar.visibility=View.INVISIBLE
    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter= HeadineAdapter(activity as MainActivity)
        recyclerView.adapter=adapter
    }

    private fun setUpViewModel() {
//        var repository= HeadlinesRepository(requireContext(),
//            ArticleDatabase(activity as MainActivity)
//        )
//        mainViewModel = ViewModelProvider(
//            requireActivity(),
//            MainViewModelFactory(repository)
//        ).get(MainViewModel::class.java)
        mainViewModel=(activity as MainActivity).mainViewModel
    }
}
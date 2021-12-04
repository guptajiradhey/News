package com.example.newsapi.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.NewsApplication
import com.example.newsapi.R
import com.example.newsapi.adapters.HeadineAdapter
import com.example.newsapi.api.ApiInterface
import com.example.newsapi.api.RetrofitHelper
import com.example.newsapi.db.ArticleDatabase
import com.example.newsapi.repository.HeadlinesRepository
import com.example.newsapi.util.Resource
import com.example.newsapi.viewmodels.MainViewModel
import com.example.newsapi.viewmodels.MainViewModelFactory
import com.example.newsapi.views.MainActivity
import com.example.newsapi.views.WebViewActivity

class BreakingNewsFragment:Fragment(){
    lateinit var  mainViewModel: MainViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: HeadineAdapter
    lateinit var  progressBar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_breaking_news, container, false)
        recyclerView = view.findViewById(R.id.rvBreakingNews)
        progressBar = view.findViewById(R.id.paginationProgressBar)
        setUpRecyclerView()
        setUpViewModel()



        adapter.setOnItenClickListener {
             val bundle=Bundle().apply {
                 putSerializable("article",it)
             }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
//            val intent=Intent(activity,WebViewActivity::class.java)
//            intent.putExtra("url",it.url)
////            intent.putExtras(bundle)
//            activity?.startActivity(intent)

        }

        mainViewModel.bNews.observe(viewLifecycleOwner, Observer { response->
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
                        Toast.makeText(activity,it,Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading->{
                    showProgreeBar()
                }

            }

        })


//
//        mainViewModel.headlineResponse.observe(viewLifecycleOwner,{
//            var list=it.articles
//
//            adapter.differ.submitList(list)
//
//
//        })
        return  view
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
        var repository= HeadlinesRepository(requireContext(),ArticleDatabase(activity as MainActivity))
        mainViewModel = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(repository)
        ).get(MainViewModel::class.java)
    }

}
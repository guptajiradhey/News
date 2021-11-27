package com.example.newsapi.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapi.R
import com.example.newsapi.views.WebViewActivity
import com.example.newsapi.model.Article

class HeadineAdapter(
    private val articleList: List<Article>,
    private val context: Context
) : RecyclerView.Adapter<HeadineAdapter.ViewHolder>() {

    class ViewHolder(
        itemView: View,
        var title: TextView = itemView.findViewById(R.id.headline_title),
        var image: ImageView = itemView.findViewById(R.id.image)) :
        RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.headline_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articleList[position]
        holder.title.text=article.title
        Glide.with(holder.image).load(article.urlToImage).into(holder.image)
        holder.itemView.setOnClickListener{
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("url", article.url)
            context.startActivity(intent)

        }


    }

    override fun getItemCount(): Int {
        return articleList.size
    }
}
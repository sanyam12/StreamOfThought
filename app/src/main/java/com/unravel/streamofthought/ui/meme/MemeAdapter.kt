package com.unravel.streamofthought.ui.meme

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.unravel.streamofthought.R
import pl.droidsonroids.gif.GifImageView

class MemeAdapter(private val posts: ArrayList<DataMeme>, private val context: Context): RecyclerView.Adapter<MemeAdapter.PostViewHolder>() {
    inner class PostViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.userName)
        val shareBt: ImageButton = view.findViewById(R.id.imageButton4)
        val imageHolder: GifImageView = view.findViewById(R.id.recyclerImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.meme_item, parent, false)
        val holder = PostViewHolder(view)
        return holder
    }



    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val curr: DataMeme = posts[position]
        val s: String = "Meme No: " + position.toString()
        holder.title.text = s
        val queue: RequestQueue = Volley.newRequestQueue(context)
        queue.add(curr.currentImageUrl)
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}
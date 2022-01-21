package com.unravel.streamofthought.ui.blog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.unravel.streamofthought.R

class BlogAdapter(private val posts: ArrayList<PostDB>, private val context: Context): RecyclerView.Adapter<BlogAdapter.PostViewHolder>() {
    inner class PostViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val displayName: TextView = view.findViewById(R.id.userName)
        val likes: TextView = view.findViewById(R.id.likeCount)
        val title: TextView = view.findViewById(R.id.postTitle)
        val likeBt: ImageView = view.findViewById(R.id.likeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        val holder = PostViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val curr_post = posts[position]
        holder.displayName.text = curr_post.displayName
        holder.likes.text = curr_post.likes
        holder.title.text = curr_post.text

        val mauth = FirebaseAuth.getInstance()
        val store = FirebaseFirestore.getInstance()
        store.collection("likes").document("uid").get()
            .addOnSuccessListener {
                val i: String= curr_post.i
                if(it.get(i)=="true")
                {
                    holder.likeBt.setImageResource(R.drawable.ic_liked)
                }else
                {
                    holder.likeBt.setImageResource(R.drawable.ic_unliked)
                }
            }
        holder.likeBt.setOnClickListener{

        }

    }

    override fun getItemCount(): Int {
        return posts.size
    }
}
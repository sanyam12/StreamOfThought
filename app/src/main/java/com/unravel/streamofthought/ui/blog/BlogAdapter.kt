package com.unravel.streamofthought.ui.blog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
        val uid = mauth.currentUser!!.uid
        val store = FirebaseFirestore.getInstance()
        val i: String= curr_post.i
        store.collection("likes").document(uid).get()
            .addOnSuccessListener {

                if(it.get(i)=="true")
                {
                    holder.likeBt.setImageResource(R.drawable.ic_liked)
                }else
                {
                    holder.likeBt.setImageResource(R.drawable.ic_unliked)
                }
            }
        holder.likeBt.setOnClickListener{
            store.collection("likes").document(uid).get()
                .addOnSuccessListener {
                    if(it.get(i)=="false")
                    {
                        val up: HashMap<String, Any> = hashMapOf()
                        up[i]=1
                        store.collection("likes").document(uid).update(up)
                        holder.likeBt.setImageResource(R.drawable.ic_liked)
                    }
                }
                .addOnFailureListener{
                    Toast.makeText(context, "this is wrong", Toast.LENGTH_SHORT).show()
                }
        }

    }

    override fun getItemCount(): Int {
        return posts.size
    }
}
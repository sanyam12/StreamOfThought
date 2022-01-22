package com.unravel.streamofthought.ui.blog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import com.unravel.streamofthought.R
import com.unravel.streamofthought.memeShare

class BlogAdapter(private val posts: ArrayList<PostDB>, private val context: Context, private val manager: FragmentManager): RecyclerView.Adapter<BlogAdapter.PostViewHolder>() {
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
        holder.title.text = curr_post.title
        
        holder.title.setOnClickListener{
//            val transaction: FragmentTransaction = manager.beginTransaction()
//            transaction.replace(R.id.main, ViewBlogFragment())
//            transaction.addToBackStack("view post")
//            transaction.commit()
            val bundle = Bundle()
            bundle.putString("displayName", curr_post.displayName)
            bundle.putString("title", curr_post.title)
            bundle.putString("text", curr_post.text)
            bundle.putString("likes", curr_post.likes)
            bundle.putString("i", curr_post.i)
            bundle.putString("uid", curr_post.uid)
            val fragment = ViewBlogFragment()
            fragment.setArguments(bundle)
            manager.beginTransaction().replace(R.id.blogF, fragment).commit()
            //Toast.makeText(context, "temp", Toast.LENGTH_SHORT).show()
        }

        val auth = FirebaseAuth.getInstance()
        val store = FirebaseFirestore.getInstance()
        val uid: String = auth.currentUser!!.uid
        val i = curr_post.i
        Toast.makeText(context,i, Toast.LENGTH_SHORT).show()
        store.collection("likes").document("uid").get()
            .addOnSuccessListener {
                if(it.get(uid)==null)
                {
                    val map: HashMap<String, Any> = hashMapOf()
                    val m: HashMap<String, Any> = hashMapOf()
                    m[i] = "false"
                    map[uid] = m
                    store.collection("likes").document("uid").set(map)
                        .addOnSuccessListener { Toast.makeText(context,"working", Toast.LENGTH_SHORT).show() }
                    holder.likeBt.setImageResource(R.drawable.ic_unliked)
                }else
                {
                    val map:HashMap<String, Any> = it.get(uid) as HashMap<String, Any>
                    if(map[i]=="true")
                    {
                        holder.likeBt.setImageResource(R.drawable.ic_liked)
                    }else
                    {
                        holder.likeBt.setImageResource(R.drawable.ic_unliked)
                        if(map[i]==null)
                        {
                            map[i] = "false"
                            val m = HashMap<String, Any>()
                            m[uid] = map
                            store.collection("likes").document("uid").update(m)
                        }
                    }
                }
            }

        holder.likeBt.setOnClickListener{
            store.collection("likes").document("uid").get()
                .addOnSuccessListener {
                    val map: HashMap<String, Any> = it.get(uid) as HashMap<String, Any>
                    if(map[i]=="true")
                    {
                        map[i] = "false"
                        val m: HashMap<String, Any> = hashMapOf()
                        m[uid] = m
                        store.collection("likes").document("uid").update(m)
                        holder.likeBt.setImageResource(R.drawable.ic_unliked)
                    }
                    else
                    {
                        map[i] = "true"
                        val m: HashMap<String, Any> = hashMapOf()
                        m[uid] = m
                        store.collection("likes").document("uid").update(m)
                        holder.likeBt.setImageResource(R.drawable.ic_liked)
                    }


                }
        }

    }

    override fun getItemCount(): Int {
        return posts.size
    }
}
package com.unravel.streamofthought.ui.blog

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.unravel.streamofthought.R

class BlogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab: FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener{
            fab.visibility = View.GONE
            val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main, CreatePostFragment())
            transaction.addToBackStack("create post")
            transaction.commit()
        }

        val manager: FragmentManager = requireActivity().supportFragmentManager

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter
        val list: ArrayList<PostDB> = arrayListOf()
        val adapter: BlogAdapter = BlogAdapter(list, view.context, manager)
        recyclerView.adapter = adapter
        val db = FirebaseFirestore.getInstance()
        db.collection("post").document("postCollection").get()
            .addOnSuccessListener {
                val users: HashMap<String, Any> = it.data as HashMap<String, Any>
                for( i in users.entries)
                {
                    val post: HashMap<String, Any> = i.value as HashMap<String, Any>
                    for(j in post.entries)
                    {
                        val k = j.value as HashMap<String, Any>
                        val item = PostDB(k["i"].toString(), k["displayName"].toString(), k["likes"].toString(), k["text"].toString(), k["uid"].toString(), k["title"].toString())
                        list.add(item)
                        adapter.notifyDataSetChanged()
                    }

                }
            }



    }

}
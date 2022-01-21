package com.unravel.streamofthought.ui.blog

import android.os.Bundle
import com.unravel.streamofthought.R
import com.unravel.streamofthought.ui.blog.daos.PostDao

class createPostActivity {
    private lateinit var postDao: PostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        postDao = PostDao()

        postButton.setOnClickListener {
            val input = postInput.text.toString().trim()
            if(input.isNotEmpty()) {
                postDao.addPost(input)
                finish()
            }
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {

    }
}
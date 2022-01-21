package com.unravel.streamofthought.ui.blog

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.unravel.streamofthought.R

class createPostActivity: AppCompatActivity() {
    //private lateinit var postDao: PostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        //postDao = PostDao()
        val postButton: Button = findViewById(R.id.postButton)
        postButton.setOnClickListener {
            val postInput: EditText = findViewById(R.id.postInput)
            val input = postInput.text.toString().trim()
            if(input.isNotEmpty()) {
                //postDao.addPost(input)
                finish()
            }
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {

    }
}
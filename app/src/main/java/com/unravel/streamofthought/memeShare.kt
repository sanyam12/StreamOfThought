package com.unravel.streamofthought

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide


class MemeShare : AppCompatActivity() {

    private var currentImageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme_share)

        loadMeme()
    }

     private fun loadMeme() {
        // Instantiate the RequestQueue.
        //val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        //progressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)
        currentImageUrl = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, currentImageUrl, null,
            Response.Listener{ response ->
                val currentImageUrl = response.getString("url")
                val memeImage = findViewById<ImageView>(R.id.memeImage)
                Glide.with(this).load(currentImageUrl).into(memeImage)
            },
            Response.ErrorListener{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            })
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

     fun shareMeme(view: android.view.View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, check this out $currentImageUrl")
        val chooser = Intent.createChooser(intent, "Share thi meme using...")
        startActivity(chooser)
    }
     fun nextMeme(view: android.view.View) {
        loadMeme()
    }
}


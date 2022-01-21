package com.unravel.streamofthought

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import javax.sql.DataSource
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class memeShare : AppCompatActivity() {

    var currentImageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme_share)

        loadMeme()
    }

     fun loadMeme() {
        // Instantiate the RequestQueue.
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)
        currentImageUrl = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, currentImageUrl, null,
            { response ->
                val url = response.getString("url")
                val memeImage = findViewById<ImageView>(R.id.memeImage)
                Glide.with(this).load(currentImageUrl).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: com.bumptech.glide.load.DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }


                }).into(memeImage)
            },
            {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            })
        // Add the request to the RequestQueue.
        queue.add(JsonObjectRequest)
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


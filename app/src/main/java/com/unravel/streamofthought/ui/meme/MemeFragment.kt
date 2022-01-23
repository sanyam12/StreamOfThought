package com.unravel.streamofthought.ui.meme

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.unravel.streamofthought.R
import com.unravel.streamofthought.databinding.FragmentMemeBinding
import android.graphics.Bitmap
import com.android.volley.*
import com.android.volley.toolbox.*
import android.graphics.BitmapFactory
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import com.bumptech.glide.Glide
import pl.droidsonroids.gif.GifImageView

class MemeFragment : Fragment() {
    private lateinit var memeFragmentViewModel: MemeFragmentViewModel
    private var _binding: FragmentMemeBinding? = null
    private val binding get() = _binding!!
    private var currentImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        memeFragmentViewModel =
            ViewModelProvider(this)[MemeFragmentViewModel::class.java]

        _binding = FragmentMemeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadMeme(view)

        val shareBt: Button = view.findViewById(R.id.shareButton)
        shareBt.setOnClickListener{shareMeme(it)}

        val nextBt: Button = view.findViewById(R.id.nextButton)
        nextBt.setOnClickListener{ loadMeme(view) }
    }

    private fun loadMeme(view: View) {
        // Instantiate the RequestQueue.
       // val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        //progressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(activity)
        val ImageUrl = "https://meme-api.herokuapp.com/gimme"


        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, ImageUrl, null,
            { response ->
                currentImageUrl = response.getString("url")
                val memeImage = view.findViewById<GifImageView>(R.id.gifmeme)
                activity?.let {
                    Glide.with(it).load(currentImageUrl).into(memeImage)
                }
            },
            {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show()
            })
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    private fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, check this out $currentImageUrl")
        val chooser = Intent.createChooser(intent, "Share this meme using...")
        startActivity(chooser)
    }

    private fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val int = Build.VERSION.SDK_INT
            if (int > 8) {
                val policy = ThreadPolicy.Builder()
                    .permitAll().build()
                StrictMode.setThreadPolicy(policy)

            }
            val url = URL(src)
            val connection: HttpURLConnection = url
                .openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
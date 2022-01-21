package com.unravel.streamofthought.ui.meme

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.unravel.streamofthought.R
import com.unravel.streamofthought.databinding.FragmentMemeBinding

class MemeFragment : Fragment() {
    private lateinit var memeFragmentViewModel: MemeFragmentViewModel
    private var _binding: FragmentMemeBinding? = null
    private val binding get() = _binding!!
    var currentImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        memeFragmentViewModel =
            ViewModelProvider(this).get(MemeFragmentViewModel::class.java)

        _binding = FragmentMemeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadMeme(view)

        val shareBt: Button = view.findViewById(R.id.shareButton)
        shareBt.setOnClickListener{shareMeme(it)}

        val nextBt: Button = view.findViewById(R.id.nextButton)
        nextBt.setOnClickListener{nextMeme(it)}
    }

    private fun loadMeme(view: android.view.View) {
        // Instantiate the RequestQueue.
        //val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        //progressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(activity)
        currentImageUrl = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, currentImageUrl, null,
            { response ->
                val url = response.getString("url")
                val memeImage = view.findViewById<ImageView>(R.id.memeImage)
                activity?.let {
                    Glide.with(it).load(currentImageUrl).listener(object: RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            //progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            //progressBar.visibility = View.GONE
                            return false
                        }


                    }).into(memeImage)
                }
            },
            {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show()
            })
        // Add the request to the RequestQueue.
        queue.add(JsonObjectRequest)
    }

    private fun shareMeme(view: android.view.View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, check this out $currentImageUrl")
        val chooser = Intent.createChooser(intent, "Share thi meme using...")
        startActivity(chooser)
    }
    private fun nextMeme(view: android.view.View) {
        loadMeme(view)
    }

}
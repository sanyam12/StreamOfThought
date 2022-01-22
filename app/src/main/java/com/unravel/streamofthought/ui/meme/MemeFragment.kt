package com.unravel.streamofthought.ui.meme

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.unravel.streamofthought.R
import com.unravel.streamofthought.databinding.FragmentMemeBinding

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
        nextBt.setOnClickListener{nextMeme(it)}
    }

    private fun loadMeme(view: View) {
        // Instantiate the RequestQueue.
       // val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        //progressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(activity)
        currentImageUrl = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, currentImageUrl, null,
            Response.Listener{ response ->
                val currentImageUrl = response.getString("url")
                val memeImage = view.findViewById<ImageView>(R.id.memeImage)
                activity?.let {
                    Glide.with(it).load(currentImageUrl).into(memeImage)
                }
            },
            Response.ErrorListener{
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show()
            })
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    private fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, check this out $currentImageUrl")
        val chooser = Intent.createChooser(intent, "Share thi meme using...")
        startActivity(chooser)
    }
    private fun nextMeme(view: View) {
        loadMeme(view)
    }

}
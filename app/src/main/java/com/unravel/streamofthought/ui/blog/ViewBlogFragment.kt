package com.unravel.streamofthought.ui.blog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import com.unravel.streamofthought.R

class ViewBlogFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_view_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scroll: ScrollView = view.findViewById(R.id.scrollView2)

        val text: String = requireArguments().getString("text")!!
        val tv:TextView = view.findViewById(R.id.textView9)
        tv.text = text


//        val title: String = requireArguments().getString("title")!!
//        val titleTv: TextView = view.findViewById(R.id.textView10)
//        titleTv.text = title

    }

}
package com.unravel.streamofthought.ui.blog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.unravel.streamofthought.R

class likesView(private val store: FirebaseFirestore,private val  newLikes: Int,private val  uid:String,private val  i: String, private val context: Context) {
    fun likeUpdate(): Boolean
    {
        var done: Boolean = false
        store.collection("post").document("postCollection").get()
            .addOnSuccessListener {
                if(it.get(uid)!=null)
                {
                    val userPosts: HashMap<String, Any> = it.get(uid) as HashMap<String, Any>
                    val postDesc: HashMap<String, Any> = userPosts[i] as HashMap<String, Any>
                    postDesc["likes"] = newLikes

                    val jam: HashMap<String, Any> = hashMapOf()
                    jam[i] = postDesc
                    val maj: HashMap<String, Any> = hashMapOf()
                    maj[uid] = jam
                    //Toast.makeText(context, newLikes.toString(), Toast.LENGTH_SHORT).show()
                    store.collection("post").document("postCollection").update(maj)
                    done = true
                }else
                {
                    //Toast.makeText(context, "it is getting null", Toast.LENGTH_SHORT).show()
                }
            }
        return done
    }
}


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


        val title: String = requireArguments().getString("title")!!
        val titleTv: TextView = view.findViewById(R.id.textView10)
        titleTv.text = title
        val username: String = requireArguments().getString("displayName")!!
        val likeBt: ImageButton = view.findViewById(R.id.imageButton)
        val likesNo: TextView = view.findViewById(R.id.textView11)
        val auth = FirebaseAuth.getInstance()
        val store = FirebaseFirestore.getInstance()
        val uid: String = auth.currentUser!!.uid
        val uidPost: String = requireArguments().getString("uid")!!
        val i = requireArguments().getString("i")!!
        likesNo.text = requireArguments().getString("likes")!!
        store.collection("likes").document("uid").get()
            .addOnSuccessListener {
                if(it.get(uid)==null)
                {
                    val map: HashMap<String, Any> = hashMapOf()
                    val m: HashMap<String, Any> = hashMapOf()
                    m[i] = "false"
                    map[uid] = m
                    store.collection("likes").document("uid").update(map)
                    likeBt.setImageResource(R.drawable.ic_unliked)
                }
                else
                {
                    val map:HashMap<String, Any> = it.get(uid) as HashMap<String, Any>
                    if(map[i]=="true")
                    {
                        likeBt.setImageResource(R.drawable.like)
                    }else
                    {
                        likeBt.setImageResource(R.drawable.ic_unliked)
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

        likeBt.setOnClickListener{
            store.collection("likes").document("uid").get()
                .addOnSuccessListener {
                    val map: HashMap<String, Any> = it.get(uid) as HashMap<String, Any>
                    if(map[i]=="true")
                    {
                        map[i] = "false"
                        val m: HashMap<String, Any> = hashMapOf()
                        m[uid] = map
                        store.collection("likes").document("uid").update(m)
                        likeBt.setImageResource(R.drawable.ic_unliked)
                        val newLikes: Int = requireArguments().getString("likes")!!.toInt() - 1
                        val clike = likesView(store,newLikes,uidPost,i, requireContext())
                        clike.likeUpdate()
                        likesNo.text = newLikes.toString()

                    }
                    else
                    {
                        map[i] = "true"
                        val m: HashMap<String, Any> = hashMapOf()
                        m[uid] = map
                        store.collection("likes").document("uid").update(m)
                        likeBt.setImageResource(R.drawable.like)
                        val newLikes: Int = requireArguments().getString("likes")!!.toInt() + 0
                        val clike = likesView(store,newLikes,uidPost,i, requireContext())
                        clike.likeUpdate()
                        likesNo.text = newLikes.toString()
                    }


                }
        }

        val shareBt: ImageButton = view.findViewById(R.id.imageButton9)
        shareBt.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Hey, check this Post on *Barefoot_LIfe* by *$username*\n\n" + title + "\n\n$text")
            val chooser = Intent.createChooser(intent, "Share this meme using...")
            startActivity(chooser)
        }

    }


}
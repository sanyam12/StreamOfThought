package com.unravel.streamofthought.ui.blog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.unravel.streamofthought.R

class CreatePostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mauth = FirebaseAuth.getInstance()
        val store = FirebaseFirestore.getInstance()
        val postText: EditText = view.findViewById(R.id.postInput)
        val postBt: Button = view.findViewById(R.id.postButton)
        postBt.setOnClickListener{
            val s: String = postText.text.toString()
            val uid = mauth.currentUser!!.uid
            if(s.isEmpty())
                Toast.makeText(activity, "Post cannot be Empty", Toast.LENGTH_SHORT).show()
            else
            {
                store.collection("desc").document(uid).get()
                    .addOnSuccessListener {
                        val displayName = it.get("displayName").toString()

                        store.collection("post").document("number").get()
                            .addOnSuccessListener {
                                val i= it.get("count").toString()

                                store.collection("post").document("postCollection").get()
                                    .addOnSuccessListener {
                                        var rest: HashMap<String, Any> = hashMapOf()
                                        if(it.get(uid)!=null)
                                        {
                                            rest= it.get(uid) as HashMap<String, Any>
                                        }

                                        val likes = 0
                                        val map = HashMap<String, Any>()
                                        map["likes"] = likes
                                        map["displayName"] = displayName
                                        map["uid"] = uid
                                        map["text"] = s
                                        val document = HashMap<String, Any>()
                                        rest[i] = map
                                        document[uid] = rest

                                        store.collection("post").document("postCollection").update(document)
                                            .addOnSuccessListener {
                                                val a = HashMap<String, Int>()
                                                a["count"] = i.toInt()+1
                                                store.collection("post").document("number").update(a as Map<String, Any>)
                                                Toast.makeText(activity, "Firestore Updated", Toast.LENGTH_SHORT).show()
                                                val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                                                transaction.replace(R.id.main, BlogFragment())
                                                transaction.commit()
                                            }
                                            .addOnFailureListener{
                                                Toast.makeText(activity, "Error here", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                            }

                    }
            }




        }
    }

}
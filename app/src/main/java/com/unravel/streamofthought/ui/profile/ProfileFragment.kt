package com.unravel.streamofthought.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import com.unravel.streamofthought.R
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.view.ContextThemeWrapper
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.unravel.streamofthought.SignIn


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val contextThemeWrapper: Context = ContextThemeWrapper(activity, R.style.splash)
        val localInflator: LayoutInflater = inflater.cloneInContext(contextThemeWrapper)
        return localInflator.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar!!.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val store: FirebaseFirestore = FirebaseFirestore.getInstance()
        val mauth: FirebaseAuth = FirebaseAuth.getInstance()
        val uid = mauth.currentUser!!.uid.toString()
        if(mauth.currentUser!=null)
        {
            store.collection("desc").document(uid).get()
                .addOnSuccessListener {
                    val name: TextView = view.findViewById(R.id.textView)
                    val mail: TextView = view.findViewById(R.id.textView6)
                    val fullName: String =  it.get("first").toString() + " " + it.get("last").toString()
                    name.text = fullName
                    mail.text = it.get("mail").toString()
                }
        }

        val switch: Switch = view.findViewById(R.id.switch1)
        switch.setOnClickListener{

        }

        val logOut: Button = view.findViewById(R.id.button2)
        logOut.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(activity, SignIn::class.java)
            intent.putExtra("bool", "true")
            startActivity(intent)
        }
    }

}
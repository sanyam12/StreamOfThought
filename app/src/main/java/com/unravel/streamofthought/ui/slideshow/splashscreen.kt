package com.unravel.streamofthought.ui.slideshow

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.telecom.Call
import android.view.Display
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.unravel.streamofthought.*

class splashscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)
        val a = 4000
        if(FirebaseAuth.getInstance().currentUser==null){
            Handler().postDelayed({
                val intent = Intent(this, SignIn ::class.java)
                startActivity(intent)
                finish()
            }, a.toLong())
        }else
        {
            val db = FirebaseFirestore.getInstance()
            val mauth = FirebaseAuth.getInstance()
            db.collection("desc").document(mauth.uid.toString()).get().addOnSuccessListener {
                if(!it.contains("displayName"))
                {
                    Handler().postDelayed({
                        val intent = Intent(this, Details::class.java)
                        startActivity(intent)
                        finish()
                    }, a.toLong())
                }else
                {
                    Handler().postDelayed({
                        val intent: Intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, a.toLong())
                }
            }

        }

//        Handler().postDelayed({
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        },a.toLong())
    }

}
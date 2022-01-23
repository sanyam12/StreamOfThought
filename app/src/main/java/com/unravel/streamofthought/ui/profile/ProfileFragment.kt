package com.unravel.streamofthought.ui.profile

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unravel.streamofthought.R
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.ContextThemeWrapper
import android.widget.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.unravel.streamofthought.SignIn
import com.unravel.streamofthought.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private lateinit var imageUri: Uri
    private var isclick: Boolean = false
    private lateinit var profileFragmentViewModel: ProfileFragmentViewModel
    private var storage: FirebaseStorage = FirebaseStorage.getInstance()
    private var storageReference = storage.getReference()


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
        val dp: ImageView = view.findViewById(R.id.imageView5)

        val store: FirebaseFirestore = FirebaseFirestore.getInstance()
        val mauth: FirebaseAuth = FirebaseAuth.getInstance()
        val dpLocation = mauth.currentUser?.uid
        val url = "images/$dpLocation/profile_pic"
        val max_bytes:Long = 1024*1024
        val imgRef = storageReference.child(url)
        imgRef.getBytes(max_bytes).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it,0,it.size)
            dp.setImageBitmap(bitmap)
        }.addOnFailureListener{
            dp.setImageResource(R.drawable.user)
        }

        val bio: TextView = view.findViewById(R.id.enterBio)
        val db = FirebaseFirestore.getInstance()
        db.collection("desc").document(mauth.uid.toString()).get().addOnSuccessListener {
            var display = it.get("bio").toString()
                bio.text = display
        }


        val uid = mauth.currentUser!!.uid.toString()
        if (mauth.currentUser != null) {
            store.collection("desc").document(uid).get()
                .addOnSuccessListener {
                    val name: TextView = view.findViewById(R.id.textView)
                    val mail: TextView = view.findViewById(R.id.textView6)
                    val fullName: String =
                        it.get("first").toString() + " " + it.get("last").toString()
                    name.text = fullName
                    mail.text = it.get("mail").toString()
                }
        }

        val switch: Switch = view.findViewById(R.id.switch1)
        switch.setOnClickListener {
            if (switch.isChecked) {
                val map: Map<String, String> = mapOf("anon" to "true")

                store.collection("desc").document(uid)
                    .update(map)
                Toast.makeText(activity, "updated", Toast.LENGTH_SHORT).show()
            } else {
                val map: Map<String, String> = mapOf("anon" to "false")

                store.collection("desc").document(uid)
                    .update(map)
                Toast.makeText(activity, "updated to false", Toast.LENGTH_SHORT).show()
            }
        }

        val logOut: Button = view.findViewById(R.id.button2)
        logOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(activity, SignIn::class.java)
            intent.putExtra("bool", "true")
            startActivity(intent)
        }


        val myButton: View = view.findViewById(R.id.imageButton3)
        myButton.setOnClickListener {
            SelectImage()
        }

        val save: Button = view.findViewById(R.id.button3)
        save.setOnClickListener {
            if (isclick)
                UploadImage()
            else
                Snackbar.make(view, "No changes to save,...", Snackbar.LENGTH_SHORT)
                    .setAction("action", null).show()
        }





    }


    private fun SelectImage() {
        val a: Intent = Intent(Intent.ACTION_GET_CONTENT).setType("image/*")
        val chooser: Intent = Intent.createChooser(a, "Select image from here")
        startActivityForResult(chooser, 100)
        isclick = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data!!
            _binding?.imageView5?.setImageURI(imageUri)

        }

    }

   private fun UploadImage() {

        if (imageUri != null) {
            val id = FirebaseAuth.getInstance().currentUser?.uid
            val ref: StorageReference = FirebaseStorage.getInstance().getReference("images/$id/profile_pic")
            ref.putFile(imageUri)
                .addOnSuccessListener {
                    Toast.makeText(activity, "Profile photo changed", Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener {
                    OnFailureListener() {
                        Toast.makeText(activity, "Upload Failed ${it.toString()}", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnProgressListener {
                    Toast.makeText(activity, "working on it", Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
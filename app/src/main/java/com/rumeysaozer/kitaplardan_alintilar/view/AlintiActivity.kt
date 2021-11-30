package com.rumeysaozer.kitaplardan_alintilar.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rumeysaozer.kitaplardan_alintilar.R
import kotlinx.android.synthetic.main.activity_alinti.*
import java.util.*

class AlintiActivity : AppCompatActivity() {
    private lateinit var storage : FirebaseStorage
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore
    var selectedImage : Uri? = null
    var selectedBitmap : Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alinti)

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
    }

    fun kaydet (view : View){
        val uuid =UUID.randomUUID()
        val imageName = "${uuid}.jpg"
        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName)
        if(selectedImage != null){
            imageReference.putFile(selectedImage!!).addOnSuccessListener { taskSnapshot ->
                val uploadedImageReference = FirebaseStorage.getInstance().reference.child("images").child(imageName)
                uploadedImageReference.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    val currentUserEmail = auth.currentUser!!.email.toString()
                    val kitapAdi = kitapAdiText.text.toString()
                    val yazarAd = yazarAdText.text.toString()
                    val alinti = alintiText.text.toString()


                    val alintiHashMap = hashMapOf<String, Any>()
                    alintiHashMap.put("imageurl", downloadUrl)
                    alintiHashMap.put("userEmail", currentUserEmail)
                    alintiHashMap.put("kitapAdi", kitapAdi)
                    alintiHashMap.put("yazarAd", yazarAd)
                    alintiHashMap.put("alinti",alinti)

                    database.collection("alinti").add(alintiHashMap).addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            finish()
                        }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()

                    }
                }.addOnFailureListener {  exception ->
                    Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()


                }
            }
        }
    }

    fun gorselEkle(view : View){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }
        else{
            val gIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(gIntent, 2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1){
            if(grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val gIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(gIntent, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
           selectedImage= data.data
            if(selectedImage != null){
                if(Build.VERSION.SDK_INT >= 28){
                    val source = ImageDecoder.createSource(this.contentResolver, selectedImage!!)
                    selectedBitmap = ImageDecoder.decodeBitmap(source)
                    imageAdd.setImageBitmap(selectedBitmap)

                }
                else{
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                imageAdd.setImageBitmap(selectedBitmap)}

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
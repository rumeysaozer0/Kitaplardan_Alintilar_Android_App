package com.rumeysaozer.kitaplardan_alintilar.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.rumeysaozer.kitaplardan_alintilar.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(this, KitaplarActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun girisYap(view : View){
        val email = mailText.text.toString()
        val password  = passwordText.text.toString()
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
         if(task.isSuccessful){
             Toast.makeText(this, "Hoşgeldiniz", Toast.LENGTH_LONG).show()
             val intent = Intent(this, KitaplarActivity::class.java)
             startActivity(intent)
             finish()
         }
        }.addOnFailureListener { exception ->
            Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }

    fun kayitOl(view : View){
        val email = mailText.text.toString()
        val password  = passwordText.text.toString()

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val intent = Intent(this, KitaplarActivity::class.java )
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, exception.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }
}
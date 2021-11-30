package com.rumeysaozer.kitaplardan_alintilar.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rumeysaozer.kitaplardan_alintilar.model.Alinti
import com.rumeysaozer.kitaplardan_alintilar.R
import com.rumeysaozer.kitaplardan_alintilar.adapter.KARecyclerAdapter
import kotlinx.android.synthetic.main.activity_kitaplar.*

class KitaplarActivity : AppCompatActivity() {
    private lateinit var recyclerViewAdapter: KARecyclerAdapter
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore
    var alintiListesi = ArrayList<Alinti>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kitaplar)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        getData()

       var layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerViewAdapter = KARecyclerAdapter(alintiListesi)
        recyclerView.adapter = recyclerViewAdapter

    }
    fun getData() {
        database.collection("alinti").addSnapshotListener { value, error ->
                if(error != null){
                    Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
                }
            else{
                if(value != null){
                    if(value.isEmpty == false){
                      val documents =  value.documents
                        alintiListesi.clear()
                        for (document in documents){
                         val kitapAdi =    document.get("kitapAdi") as String
                            val yazarAd = document.get("yazarAd") as String
                            val alinti = document.get("alinti") as String
                            val imageurl = document.get("imageurl") as String

                            val indirilen = Alinti(alinti, kitapAdi, yazarAd,imageurl)
                            alintiListesi.add(indirilen)

                        }
                       recyclerViewAdapter.notifyDataSetChanged()
                    }
                }
                }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.options_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.kitap_ekle){
            val intent = Intent(this, AlintiActivity::class.java)
            startActivity(intent)

        }
        else if(item.itemId == R.id.cikis_yap){
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
        return super.onOptionsItemSelected(item)
    }
}
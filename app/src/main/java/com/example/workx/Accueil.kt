package com.example.workx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup

import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workx.Repository.AdRepository
import com.example.workx.adapter.Ad_adapter
import com.example.workx.holder.AdViewHolder

import com.example.workx.model.Ad
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Accueil : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var adRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var search_text: EditText
    private lateinit var searchBtn: ImageButton
    private lateinit var fabCreateAd: FloatingActionButton
    private lateinit var adapter: FirebaseRecyclerAdapter<Ad, AdViewHolder>
    private lateinit var btnOpenSideBar : ImageButton
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        btnOpenSideBar = findViewById(R.id.btnOpenSidebar)
        searchBtn = findViewById(R.id.searchBtn)
        search_text = findViewById(R.id.search_text)
        recyclerView = findViewById(R.id.recyclerView)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adRef = database.getReference("Ad")
        fabCreateAd = findViewById(R.id.fabCreateAd)
        navigationView = findViewById(R.id.navigationView)

        navigationView.setNavigationItemSelectedListener {menuItem->
            when (menuItem.itemId){
                R.id.logout->{
                    mAuth.signOut()
                    Toast.makeText(this,"Déconnexion réussie", Toast.LENGTH_SHORT).show()
                    val intent= Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setNavigationItemSelectedListener true
                }
                else-> return@setNavigationItemSelectedListener false
            }
        }

        btnOpenSideBar.setOnClickListener{
            openSideBar()
        }

        fabCreateAd.setOnClickListener {
            val intent = Intent(this, CreateAd::class.java)
            startActivity(intent)
        }

        searchBtn.setOnClickListener {
            val searchText = search_text.text.toString().trim()
            performSearch(searchText)
        }

        val currentUser: FirebaseUser? = mAuth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            val options = FirebaseRecyclerOptions.Builder<Ad>()
                .setQuery(adRef, Ad::class.java)
                .build()

            adapter = object : FirebaseRecyclerAdapter<Ad, AdViewHolder>(options) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
                    val view =
                        LayoutInflater.from(parent.context).inflate(R.layout.ad_item, parent, false)
                    return AdViewHolder(view)
                }

                override fun onBindViewHolder(holder: AdViewHolder, position: Int, model: Ad) {
                    holder.bind(model)
                    Log.d("TAG", "Ad retrieved: ${model.toString()}")

                    holder.adBtn.setOnClickListener {
                        val intent = Intent(this@Accueil, Ad_Detail::class.java)
                        intent.putExtra(
                            "adId",
                            getRef(position).key
                        ) // Utilisation de la clé Firebase comme identifiant
                        startActivity(intent)
                    }

                }

            }
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)


        }
    }

    private fun openSideBar() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        drawerLayout.openDrawer(Gravity.LEFT)
    }

    private fun performSearch(query: String) {
        Log.d("TAG", "Search query: $query")

        val queryRef = adRef.orderByChild(" nameAd").startAt(query).endAt(query + "\uf8ff")
        Log.d("TAG", "Query: $queryRef")

        val options = FirebaseRecyclerOptions.Builder<Ad>()
            .setQuery(queryRef, Ad::class.java)
            .build()

        adapter.updateOptions(options)

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }


}
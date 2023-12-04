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
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workx.Repository.AdRepository
import com.example.workx.adapter.AdAdapter
import com.example.workx.holder.AdViewHolder
import com.example.workx.holder.AllAdsFragment

import com.example.workx.model.Ad
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.android.material.bottomnavigation.BottomNavigationView
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


    private lateinit var mAuth: FirebaseAuth
    private lateinit var bottomNav : BottomNavigationView
    private lateinit var frameLayout: FrameLayout
    private lateinit var btnAdd:FloatingActionButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        btnAdd = findViewById(R.id.fabAddBtn)
        bottomNav = findViewById(R.id.bottomNavigationView)
        frameLayout = findViewById(R.id.frameLayout)
        mAuth = FirebaseAuth.getInstance()

        btnAdd.setOnClickListener {
            val intent = Intent(this, CreateAd::class.java)
            startActivity(intent)
        }
        val currentUser: FirebaseUser? = mAuth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            bottomNav.setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_home -> loadFragment(AllAdsFragment(), false)
                    R.id.menu_logout -> {
                        mAuth.signOut()
                        Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    R.id.menu_profile-> loadFragment(MyAdsFragment(), false)
                }
                true
            }
            loadFragment(AllAdsFragment(), false)
        }










    }

    private fun loadFragment(fragment: Fragment, isAppInitialized: Boolean){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if(isAppInitialized){
            fragmentTransaction.add(R.id.frameLayout, fragment)
        }else{
            fragmentTransaction.replace(R.id.frameLayout, fragment)
        }

        fragmentTransaction.commit()
    }




   /* private fun performSearch(query: String) {
        Log.d("TAG", "Search query: $query")

        val queryRef = adRef.orderByChild(" nameAd").startAt(query).endAt("\uf8ff"+query + "\uf8ff")

        queryRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
              adsList.clear()

                for (adSnapshot in snapshot.children){
                    val ad = adSnapshot.getValue(Ad::class.java)
                    if(ad!=null){
                        adsList.add(ad)
                    }
                }
                adapter.setAds(adsList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SearchFirebaseError", "Erreur dans la recherche ${error.message}")
            }
        })

    }*/




}
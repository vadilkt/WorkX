package com.example.workx.Repository

import android.util.Log
import com.example.workx.model.Ad
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdRepository {
    private val adRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Ad")

    fun searchAds(query:String, callback: (List<Ad>)->Unit){
        val searchResults : MutableList<Ad> = mutableListOf()

        adRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (adSnapshot in snapshot.children){
                    val ad = adSnapshot.getValue(Ad::class.java)

                    if (ad?.nameAd?.contains(query, ignoreCase = true)==true || ad?.detailAd?.contains(query, ignoreCase = true)==true){
                        searchResults.add(ad)
                    }
                }
                callback(searchResults)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("AdRepository", "Erreur de base de données: ${error.message}")
            }
        })
    }
}
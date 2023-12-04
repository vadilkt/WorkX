package com.example.workx.holder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workx.MainActivity
import com.example.workx.R
import com.example.workx.adapter.AdAdapter
import com.example.workx.model.Ad
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllAdsFragment : Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var adRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdAdapter
    private lateinit var adsList: MutableList<Ad>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_all_ads, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adRef = database.reference.child("Ad")
        adsList = mutableListOf()

        val currentUser: FirebaseUser? = mAuth.currentUser
        if (currentUser == null) {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        } else {
            adapter = AdAdapter(requireActivity())
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            fetchAdsFromFirebase()
        }

        return view
    }

    private fun fetchAdsFromFirebase() {
        adRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val allAdsList = mutableListOf<Ad>()


                    for (adSnapshot in snapshot.children) {
                        val adId = adSnapshot.key

                        val adAttributes = adSnapshot.getValue(Ad::class.java)

                        adAttributes?.let {
                            allAdsList.add(
                                Ad(
                                    adId,
                                    it.userId,
                                    it.nameAd,
                                    it.priceAd,
                                    it.telAd,
                                    it.detailAd,
                                    it.categoryAd,
                                    it.locationAd,
                                    it.imageAd
                                )
                            )
                        }
                    }

                adsList.clear()
                adsList.addAll(allAdsList)
                adapter?.let{
                    it.setAds(adsList)
                    Log.d("FirebaseData", "Nombre d'annonces récupérées: ${adsList.size}")
                } }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Erreur de lecture depuis Firebase: ${error.message}" )
            }
        })
    }




}

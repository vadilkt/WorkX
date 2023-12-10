package com.example.workx.fragments

import android.content.Intent
import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.launch
import java.util.Locale

class AllAdsFragment : Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var adRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdAdapter
    private lateinit var adsList: MutableList<Ad>
    private lateinit var loadingBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_all_ads, container, false)

        loadingBar = view.findViewById(R.id.loadingBar)
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

            lifecycleScope.launch {

                fetchAdsFromFirebase()

            }

        }

        return view
    }

    private fun fetchAdsFromFirebase() {
        loadingBar.visibility=View.VISIBLE
        adRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val allAdsList = mutableListOf<Ad>()


                    for (adSnapshot in snapshot.children) {
                        val adId = adSnapshot.key

                        val adAttributes = adSnapshot.getValue(Ad::class.java)

                        adAttributes?.let {
                            val formattedPrice = displayPrice(it.priceAd)
                            allAdsList.add(
                                Ad(
                                    adId,
                                    it.userId,
                                    it.nameAd,
                                    formattedPrice,
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
                    loadingBar.visibility = View.GONE
                    it.setAds(adsList)
                    Log.d("FirebaseData", "Nombre d'annonces récupérées: ${adsList.size}")
                } }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Erreur de lecture depuis Firebase: ${error.message}" )
            }
        })
    }

    private fun displayPrice(price: String?): String? {
        val priceValue = price?.toDoubleOrNull()

        return if(priceValue!=null){
            val locale = Locale("fr","CM")
            val currency = Currency.getInstance(locale)
            val currencyFormat = NumberFormat.getCurrencyInstance(locale)
            currencyFormat.currency = currency

            currencyFormat.format(priceValue)
        }else{
            price
        }
    }


}

package com.example.workx

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workx.adapter.AdAdapter
import com.example.workx.model.Ad
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MyAdsFragment : Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var adRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdAdapter
    private lateinit var adsList: MutableList<Ad>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_ads, container, false)

        return view
    }

}
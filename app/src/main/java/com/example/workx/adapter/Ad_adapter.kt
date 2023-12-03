package com.example.workx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workx.R
import com.example.workx.holder.AdViewHolder
import com.example.workx.model.Ad

class Ad_adapter (private val context: Context):RecyclerView.Adapter<AdViewHolder>(){

    private var ads: List<Ad> = emptyList()

    fun setAds(newAds: List<Ad>){
        ads = newAds
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ad_item, parent, false)
        return AdViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
    val ad = ads[position]
    holder.bind(ad)
    }

    override fun getItemCount(): Int {
        return ads.size
    }

}
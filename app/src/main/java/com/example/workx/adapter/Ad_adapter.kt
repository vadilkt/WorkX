package com.example.workx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workx.R
import com.example.workx.holder.AdViewHolder
import com.example.workx.model.Ad

class AdAdapter (private val context: Context):RecyclerView.Adapter<AdViewHolder>(){

    private var adsList:MutableList<Ad> = mutableListOf()

    fun setAds(newAds: List<Ad>?){
        adsList.clear()
        newAds?.let {
            adsList.addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.ad_item, parent, false)
        return AdViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
    val ad = adsList[position]
    holder.bind(ad)
    }

    override fun getItemCount(): Int {
        return adsList.size
    }

}
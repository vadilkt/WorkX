package com.example.workx.holder

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.workx.Ad_Detail
import com.example.workx.R
import com.example.workx.model.Ad
import com.squareup.picasso.Picasso

class AdViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
    val context: Context = itemView.context
    val adName= itemView.findViewById<TextView>(R.id.ad_name)
    val adPrice= itemView.findViewById<TextView>(R.id.ad_price)
    val adDetail= itemView.findViewById<TextView>(R.id.ad_details)
    val adLocation= itemView.findViewById<TextView>(R.id.ad_location)
    val adBtn = itemView.findViewById<Button>(R.id.ad_btn)
    val imageView = itemView.findViewById<ImageView>(R.id.ad_image)
    val adDeleteBtn = itemView.findViewById<Button>(R.id.btnDelete)

    fun bind(ad: Ad){
        val imageUrl =  ad.imageAd?.firstOrNull()

        if(!imageUrl.isNullOrEmpty()){
            Picasso.get()
                .load(imageUrl)
                .into(imageView)
        }else{
            imageView.setImageResource(R.drawable.add_img)
        }

        adName.text = ad.nameAd?:""
        adPrice.text = ad.priceAd?:""
        adLocation.text = ad.locationAd?:""
        adDetail.text = ad.detailAd?:""

        adDeleteBtn.setOnClickListener {

        }
        adBtn.setOnClickListener {
            val intent = Intent(context, Ad_Detail::class.java)
            intent.putExtra("adId", ad.nameAd)

            context.startActivity(intent)
            Log.d("AdViewHolder", "Ad Name: ${ad.nameAd}")
            Log.d("AdViewHolder", "Ad Price: ${ad.priceAd}")
            Log.d("AdViewHolder", "Ad Location: ${ad.locationAd}")
            Log.d("AdViewHolder", "Ad Detail: ${ad.detailAd}")
        }
    }

}
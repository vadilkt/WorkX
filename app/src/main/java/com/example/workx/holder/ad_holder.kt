package com.example.workx.holder

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
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
            val phoneNumber = ad.telAd
            if(!phoneNumber.isNullOrEmpty()){
                Log.d("AdViewHolder", "Phone number: $phoneNumber")
                if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED){
                    Log.d("AdViewHolder", "Permission granted. Starting call intent.")
                    context.startActivity(createCallIntent(phoneNumber))
                }else{
                    Log.d("AdViewHolder", "Requesting CALL_PHONE permission.")
                    ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.CALL_PHONE), REQUEST_CALL_PHONE_PERMISSION)
                }
            }else {
            Log.e("AdViewHolder", "Phone number is null or empty.")
        }
        }
    }

    private fun createCallIntent(phoneNumber: String): Intent {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:$phoneNumber")
        return callIntent
    }

    companion object{
        const val REQUEST_CALL_PHONE_PERMISSION = 1
    }

}
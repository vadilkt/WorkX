package com.example.workx.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Ad(
    val adId:String?="",
    val userId:String?="",
    val nameAd:String?="",
    val priceAd:String?="",
    val telAd:String?="",
    val detailAd : String?="",
    val categoryAd:String?="",
    val locationAd:String?="",
    val imageAd: MutableList<String>?= mutableListOf()
) {
    constructor() : this("","","", "", "", "", "", "", mutableListOf())
}


package com.example.workx

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.workx.model.Ad
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class CreateAd : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var storage : FirebaseStorage
    private lateinit var btnSelectedImage: Button
    private lateinit var btnCreateAd: Button
    private lateinit var adName : EditText
    private lateinit var adPrice: EditText
    private lateinit var adPhone : EditText
    private lateinit var adLocation : EditText
    private lateinit var adCategory : Spinner
    private lateinit var adDetail: EditText


    private val images: MutableList<String> = mutableListOf()
    private lateinit var gallerylauncher : ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_add)

        adName = findViewById(R.id.adName)
        adPrice = findViewById(R.id.adPrice)
        adPhone = findViewById(R.id.adPhoneNumber)
        adLocation = findViewById(R.id.adLocation)
        adDetail = findViewById(R.id.adDetails)
        adCategory = findViewById(R.id.spinnerCategory)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        btnSelectedImage = findViewById(R.id.btnSelectImage)
        btnCreateAd = findViewById(R.id.btnCreateAd)

        gallerylauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if (result.resultCode== Activity.RESULT_OK){
                val data: Intent?=result.data
                val selectedImgUri = data?.data
                if(selectedImgUri !=null){
                    val imageUrl = selectedImgUri.toString()
                    images.add(imageUrl)
                }
            }
        }

        val categories = resources.getStringArray(R.array.categories)
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adCategory.adapter = categoryAdapter

        btnSelectedImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type="image/*"
            gallerylauncher.launch(galleryIntent)
        }

        btnCreateAd.setOnClickListener {
            createdAd()
        }


    }

    private fun createdAd() {
        val adName = adName.text.toString().trim()
        val adPrice = adPrice.text.toString().trim()
        val adPhoneNumber = adPhone.text.toString().trim()
        val selectedCategory = adCategory.selectedItem.toString()
        val adDetail= adDetail.text.toString().trim()
        val adLocation= adLocation.text.toString().trim()

        if(adName.isBlank() || adPrice.isBlank() || adPhoneNumber.isBlank() || adLocation.isBlank()){
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }
        val ad = Ad(adName,adPrice, adPhoneNumber, adDetail, selectedCategory, adLocation, images)

        val userId = auth.currentUser?.uid
        if(userId!=null){
            val adRef = database.getReference("Ad").child(userId).push()
            adRef.setValue(ad)
            Toast.makeText(this, "Votre annonce a été enregistrée avec succès !", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, Accueil::class.java))
            finish()

        }
    }
}
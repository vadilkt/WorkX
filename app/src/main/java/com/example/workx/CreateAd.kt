package com.example.workx

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import com.example.workx.model.Ad
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException
import java.util.UUID

class CreateAd : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var btnSelectedImage: Button
    private lateinit var btnCreateAd: Button
    private lateinit var adName: EditText
    private lateinit var adPrice: EditText
    private lateinit var adPhone: EditText
    private lateinit var adLocation: EditText
    private lateinit var adCategory: Spinner
    private lateinit var adDetail: EditText
    private val images: MutableList<Uri> = mutableListOf()
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var progressBar: ProgressBar
    private val storageRef = FirebaseStorage.getInstance().reference.child("images")

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
        progressBar = findViewById(R.id.progressBar)

        val categories = resources.getStringArray(R.array.categories)
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adCategory.adapter = categoryAdapter

        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    val selectedImages: ArrayList<Uri>? = if (data?.clipData != null) {
                        val imageCount = data.clipData!!.itemCount
                        val images = ArrayList<Uri>()
                        for (i in 0 until imageCount) {
                            val imageUri = data.clipData!!.getItemAt(i).uri
                            images.add(imageUri)
                        }
                        images
                    } else {
                        val singleImage = data?.data
                        if (singleImage != null) {
                            arrayListOf(singleImage)
                        } else {
                            null
                        }
                    }
                    selectedImages?.let {
                        images.addAll(it)
                    }
                }
            }

        btnSelectedImage.setOnClickListener {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.type = "image/*"
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            galleryLauncher.launch(galleryIntent)
        }

        btnCreateAd.setOnClickListener {
            if (images.isNotEmpty()) {
                uploadImagesToStorage(images)
            } else {
                Toast.makeText(this, "Veuillez sélectionner au moins une image", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun uploadImagesToStorage(images: List<Uri>) {
        val imagesUrls = mutableListOf<String>()
        progressBar.visibility = View.VISIBLE

        for ((index, imageUri) in images.withIndex()) {
            val imageRef = storageRef.child("image_${UUID.randomUUID()}.jpg")
            try {
                val inputStream = contentResolver.openInputStream(imageUri)
                inputStream?.let {
                    imageRef.putStream(it)
                        .addOnSuccessListener {
                            imageRef.downloadUrl.addOnSuccessListener { uri ->
                                imagesUrls.add(uri.toString())
                                Log.d(
                                    "FirebaseUpload",
                                    "Image $index uploaded successfully. URL: ${uri.toString()}"
                                )

                                if (imagesUrls.size == images.size) {
                                    createAd(imagesUrls)
                                    progressBar.visibility = View.GONE
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                this,
                                "Une erreur est survenue lors de la création de l'annonce!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } ?: run {
                    Log.e("FirebaseUpload", "Failed to open input stream for image $index")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun createAd(imagesUrls: MutableList<String>) {
        val adName = adName.text.toString().trim()
        val adPrice = adPrice.text.toString().trim()
        val adPhoneNumber = adPhone.text.toString().trim()
        val selectedCategory = adCategory.selectedItem.toString()
        val adDetail = adDetail.text.toString().trim()
        val adLocation = adLocation.text.toString().trim()


        if (adName.isBlank() || adPrice.isBlank() || adPhoneNumber.isBlank() || adLocation.isBlank()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = auth.currentUser?.uid

        if (userId != null) {
            val adRef = database.getReference("Ad").push()
            val adId = adRef.key
            val ad = Ad(
                adId,
                userId,
                adName,
                adPrice,
                adPhoneNumber,
                adDetail,
                selectedCategory,
                adLocation,
                imagesUrls
            )

            adRef.setValue(ad)
            Toast.makeText(
                this,
                "Votre annonce a été enregistrée avec succès !",
                Toast.LENGTH_SHORT
            ).show()

            startActivity(Intent(this, Accueil::class.java))
            finish()
        }
    }
}

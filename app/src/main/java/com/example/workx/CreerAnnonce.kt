package com.example.workx

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.widget.Toolbar
import androidx.activity.result.contract.ActivityResultContracts

class CreerAnnonce : AppCompatActivity() {
    private lateinit var editTextTitre: EditText
    private lateinit var editTextDescriptor: EditText
    private lateinit var editTextDate: EditText
    private lateinit var imageView2: ImageView // Déclaration de la variable
    private lateinit var getContent: ActivityResultLauncher<String>

    // Constante pour identifier la demande de sélection d'image
    private val PICK_IMAGE_REQUEST = 1

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creer_annonce)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Si vous avez besoin du bouton de retour

        //gerer les edits text
        editTextTitre = findViewById(R.id.editTextTitre)
        editTextDescriptor = findViewById(R.id.editTextDescription)

        imageView2 = findViewById(R.id.imageView2)


        //gerer la selection des Categories
        val spinnerCategorie: Spinner = findViewById(R.id.spinnerCategorie)

        val options2 = arrayOf("Catégorie 1", "Catégorie 2", "Catégorie 3") // Remplacez cela par vos catégories réelles

        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, options2)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategorie.adapter = adapter2



        //Selection de la date

        val editTextDate: EditText = findViewById(R.id.editTextDate)

        editTextDate.setOnClickListener {
            showDatePickerDialog()
        }

        imageView2 = findViewById(R.id.imageView2)
        // Selection de l'image depuis la galerie
        //initialisation du listener de clic
        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            // Ici, vous obtiendrez l'URI de l'image sélectionnée dans 'uri'
            // Utilisez cette URI pour afficher l'image dans votre ImageView
            if (uri != null) {
                imageView2.setImageURI(uri)
            }
        }


        val buttonAjouterAnnonce: Button = findViewById(R.id.buttonAjouterAnnonce)
        buttonAjouterAnnonce.setOnClickListener {
            // Récupérez les valeurs des champs
            val titre = editTextTitre.text.toString()
            val description = editTextDescriptor.text.toString()
            val date = editTextDate.text.toString()
            val categorie = spinnerCategorie.selectedItem.toString()

            // Vérifiez si les champs sont remplis
            if (titre.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty() && categorie.isNotEmpty() ) {
                // Affichez une boîte de dialogue (popup) indiquant le succès
                showSuccessDialog()
            } else {
                // Affichez un message d'erreur si certains champs sont vides
                showErrorDialog()
            }
        }
    }

    //fonction de selction de la date
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear" // Format de la date
                editTextDate.setText(selectedDate)
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    //Fonction pour ouvrir la galerie
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        getContent.launch("image/*")
    }

    //Gestion  du résultat de la sélection d'image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri: Uri = data.data!!
            imageView2.setImageURI(imageUri)
        }


    }

    // Fonction pour afficher la boîte de dialogue de succès
    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Succès")
            .setMessage("L'annonce a été ajoutée avec succès!")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // Fonction pour afficher la boîte de dialogue d'erreur
    private fun showErrorDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Erreur")
            .setMessage("Veuillez remplir tous les champs!")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }



}
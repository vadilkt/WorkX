package com.example.workx

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import com.example.workx.model.UserData
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {

    private lateinit var etLastName: EditText
    private lateinit var etFirstName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etCPassword: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etButton: Button
    private lateinit var viewLogin: TextView
    private lateinit var signUpProgress: ProgressBar
    private var lastname: String = ""
    private var firstname: String = ""
    private var email: String = ""
    private var password: String = ""
    private var coPassword: String = ""
    private var phonenumber: String = ""

    //Firebase
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        signUpProgress = findViewById(R.id.signUp_Progress)
        etLastName = findViewById(R.id.etLastName)
        etFirstName = findViewById(R.id.etFirstName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etCPassword = findViewById(R.id.etConfirmPassword)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etButton = findViewById(R.id.btnRegister)
        viewLogin = findViewById(R.id.tvLoginPrompt)

        //Création des objets firebase
        mAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseRef = firebaseDatabase.getReference("UserData")

        viewLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        etButton.setOnClickListener {
            if (!validateLastName() || !validateFirstName() || !validateEmail() || !validatePhone() || !validatePass()) {
                return@setOnClickListener
            }

            if (etPassword.text.toString() == etCPassword.text.toString()) {
                signUpProgress.visibility = View.VISIBLE
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { it ->
                        try {
                            if (it.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Enregistrement en cours...",
                                    Toast.LENGTH_SHORT
                                ).show()
                                 val data = UserData(
                                     etFirstName.text.toString().trim(),
                                     etLastName.text.toString().trim(),
                                     etEmail.text.toString().trim(),
                                     etPhoneNumber.text.toString().trim()
                                 )
                                FirebaseDatabase.getInstance().getReference("UserData")
                                    .child(FirebaseAuth.getInstance().currentUser?.uid!!)
                                    .setValue(data)
                                    .addOnCompleteListener { it ->
                                        signUpProgress.visibility = View.GONE
                                        if (it.isSuccessful) {
                                            Toast.makeText(
                                                this,
                                                "Enregistrement Réussi !",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            val intent = Intent(this, Accueil::class.java)
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "Enregistrement échoué !",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        //
                                        Log.e(
                                            "Firebase",
                                            "Erreur lors de l'enregistrement dans la base de données",
                                            e
                                        )
                                    }
                            } else {
                                signUpProgress.visibility = View.GONE
                                Toast.makeText(
                                    this,
                                    "Vérifier votre email ou votre mot de passe !",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
            } else {
                Toast.makeText(this, "Les mots de passe ne correspondent pas !", Toast.LENGTH_SHORT)
                    .show()

            }
        }
    }

    private fun validateFirstName(): Boolean {
        firstname = etFirstName.text.toString().trim()
        return if (TextUtils.isEmpty(firstname)) {
            Toast.makeText(this, "Entrez votre Nom", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    private fun validateLastName(): Boolean {
        lastname = etLastName.text.toString().trim()
        return if (TextUtils.isEmpty(lastname)) {
            Toast.makeText(this, "Entrez votre Prénom", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    private fun validateEmail(): Boolean {
        email = etEmail.text.toString().trim()
        return if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Entrez votre adresse mail !", Toast.LENGTH_SHORT).show()
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Veuillez entrer une adresse mail valide !", Toast.LENGTH_SHORT)
                .show()
            false
        } else {
            true
        }
    }

    private fun validatePhone(): Boolean {
        phonenumber = etPhoneNumber.text.toString().trim()
        return if (TextUtils.isEmpty(phonenumber)) {
            Toast.makeText(this, "Entrez votre Nom", Toast.LENGTH_SHORT).show()
            false
        } else if (!Patterns.PHONE.matcher(phonenumber).matches()) {
            Toast.makeText(
                this,
                "Veuillez entrer un numéro de téléphone valide !",
                Toast.LENGTH_SHORT
            ).show()
            false
        } else {
            true
        }
    }

    private fun validatePass(): Boolean {
        password = etPassword.text.toString().trim()
        coPassword = etCPassword.text.toString().trim()

        return when {
            TextUtils.isEmpty(password) -> {
                Toast.makeText(this, "Entrez votre mot de passe", Toast.LENGTH_SHORT).show()
                false
            }

            TextUtils.isEmpty(coPassword) -> {
                Toast.makeText(this, "Confirmer votre mot de passe", Toast.LENGTH_SHORT).show()
                false
            }

            password.length <= 6 -> {
                Toast.makeText(this, "Votre mot de passe est trop court", Toast.LENGTH_SHORT).show()
                false
            }

            else -> true

        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(this, Accueil::class.java)
            startActivity(intent)
        }
    }



}
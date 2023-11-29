package com.example.workx

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Accueil : AppCompatActivity() {
    private lateinit var txtUserMail: TextView
    private lateinit var btnUserLogout : Button
    private lateinit var btnAnnonce : Button
    private lateinit var firebaserUser: FirebaseUser
    private lateinit var mAuth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        txtUserMail = findViewById(R.id.txt_userEmail)
        btnUserLogout = findViewById(R.id.btn_userlogout)
        btnAnnonce = findViewById(R.id.btn_annonce)

        mAuth = FirebaseAuth.getInstance()
        firebaserUser = mAuth.currentUser!!

        txtUserMail.text = firebaserUser.email


        btnUserLogout.setOnClickListener {
            mAuth.signOut()
            Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnAnnonce.setOnClickListener {
            val intent = Intent(this, CreerAnnonce::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed(){
        super.onBackPressed()
        finishAffinity()
    }
}
package com.example.workx
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var tvRedirectSignUp : TextView
    lateinit var etEmail : EditText
    private lateinit var etPass: EditText
    lateinit var btnLogin: Button
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvRedirectSignUp = findViewById(R.id.tvRedirectSignUp)
        btnLogin = findViewById(R.id.btnLogin)
        etEmail = findViewById(R.id.etEmailAddress)
        etPass = findViewById(R.id.etPassword)

        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener{
            login()
        }

        tvRedirectSignUp.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun login() {
        val email = etEmail.text.toString()
        val pass = etPass.text.toString()

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this){
            if(it.isSuccessful){
                Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Connexion échouée, veuillez réessayer", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
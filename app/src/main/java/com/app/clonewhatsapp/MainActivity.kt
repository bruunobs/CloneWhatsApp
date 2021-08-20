package com.app.clonewhatsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.clonewhatsapp.databinding.ActivityMainBinding
import com.app.clonewhatsapp.login.LoginActivity
import com.app.clonewhatsapp.principal.PrincipalActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonConcordo.setOnClickListener {
            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        auth = FirebaseAuth.getInstance()

    }
    /*
    override fun onStart() {
        super.onStart()
        if (auth.currentUser!! != null){
            finish()
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)

        }
    }
     */


}
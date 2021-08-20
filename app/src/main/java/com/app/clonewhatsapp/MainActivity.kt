package com.app.clonewhatsapp

import android.R
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.app.clonewhatsapp.databinding.ActivityMainBinding
import com.app.clonewhatsapp.login.LoginActivity
import com.app.clonewhatsapp.principal.PrincipalActivity
import com.google.firebase.auth.FirebaseAuth
import java.lang.Boolean


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        val user = auth.currentUser
        if (user != null){
            startActivity(Intent(this@MainActivity,PrincipalActivity::class.java))
            finish()
        }


        binding.buttonConcordo.setOnClickListener {
            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    var prevStarted = "yes"
    override fun onResume() {
        super.onResume()
        val sharedpreferences: SharedPreferences = getSharedPreferences(getString(com.app.clonewhatsapp.R.string.app_name), Context.MODE_PRIVATE)
        if (!sharedpreferences.getBoolean(prevStarted, false)) {
            var editor = sharedpreferences.edit()
            editor.putBoolean(prevStarted, Boolean.TRUE);
            editor.apply();
        } else {
            startActivity(Intent(this@MainActivity,LoginActivity::class.java))
            finish()
        }
    }
    }



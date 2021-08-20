package com.app.clonewhatsapp.principal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.app.clonewhatsapp.R

class PrincipalActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        toolbar = findViewById(R.id.toolbarPrincipal)
        toolbar.setTitle("WhatsApp")
        setSupportActionBar(toolbar) 
    }
}
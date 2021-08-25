package com.app.clonewhatsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.app.clonewhatsapp.databinding.ActivityConfiguracoesBinding

class ConfiguracoesActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    lateinit var binding: ActivityConfiguracoesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityConfiguracoesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbarPrincipal)
        toolbar.title = ""
        setSupportActionBar(toolbar)

        binding.backArrow.setOnClickListener {
            onBackPressed()
        }

        binding.ImagemPerfil.setOnClickListener{
            startActivity(Intent(this@ConfiguracoesActivity,PerfilActivity::class.java))
        }

        binding.linearLayoutPerfil.setOnClickListener {
            startActivity(Intent(this@ConfiguracoesActivity,PerfilActivity::class.java))
        }
    }
}
package com.app.clonewhatsapp.cadastro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.databinding.ActivityCadastroBinding
import com.app.clonewhatsapp.login.LoginActivity

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*
        binding.textEntrarLogin.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

         */
    }
}
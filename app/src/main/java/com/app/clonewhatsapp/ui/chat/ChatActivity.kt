package com.app.clonewhatsapp.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.databinding.ActivityChatBinding
import com.app.clonewhatsapp.model.Usuario
import com.app.clonewhatsapp.ui.contatos.ContatosActivity

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbarPrincipal)
        toolbar.title = ""


        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        val ss:String = intent.getStringExtra("chatContato").toString()
//        binding.nomeChat.text = ss


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
package com.app.clonewhatsapp.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.clonewhatsapp.R
import androidx.appcompat.widget.Toolbar
import com.app.clonewhatsapp.databinding.ActivityChatBinding

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


    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
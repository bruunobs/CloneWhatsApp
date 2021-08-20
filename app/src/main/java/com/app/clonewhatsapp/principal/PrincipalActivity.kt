package com.app.clonewhatsapp.principal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Switch
import androidx.appcompat.widget.Toolbar
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class PrincipalActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        toolbar = findViewById(R.id.toolbarPrincipal)
        toolbar.setTitle("WhatsApp")
        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuSair -> {
                deslogarUsuario()

            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun deslogarUsuario(){
        try {
            auth.signOut()
            startActivity(Intent(this@PrincipalActivity, LoginActivity::class.java))
            finish()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}
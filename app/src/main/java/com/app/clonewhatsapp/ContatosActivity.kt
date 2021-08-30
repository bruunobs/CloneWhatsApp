package com.app.clonewhatsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.app.clonewhatsapp.databinding.ActivityCadastroBinding
import com.app.clonewhatsapp.databinding.ActivityConfiguracoesBinding
import com.app.clonewhatsapp.databinding.ActivityContatosBinding

class ContatosActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    lateinit var binding: ActivityContatosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContatosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbarPrincipal)
        toolbar.title = ""
        setSupportActionBar(toolbar)



        binding.backArrowContatos.setOnClickListener {
            onBackPressed()
        }

    }

    //Configuração Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contatos,menu)
        return super.onCreateOptionsMenu(menu)
    }
/*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.menuConfiguracoes -> {
                startActivity(Intent(this,ConfiguracoesActivity::class.java))
            }

        }
        return super.onOptionsItemSelected(item)
    }

 */
}
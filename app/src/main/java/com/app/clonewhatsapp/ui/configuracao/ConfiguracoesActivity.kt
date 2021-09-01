package com.app.clonewhatsapp.ui.configuracao

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.databinding.ActivityConfiguracoesBinding
import com.app.clonewhatsapp.model.Usuario
import com.app.clonewhatsapp.ui.perfil.PerfilActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ConfiguracoesActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    lateinit var binding: ActivityConfiguracoesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfiguracoesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbarPrincipal)
        toolbar.title = "Configuracões"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        getData()


        binding.ImagemPerfil.setOnClickListener {
            startActivity(Intent(this@ConfiguracoesActivity, PerfilActivity::class.java))
        }

        binding.linearLayoutPerfil.setOnClickListener {
            startActivity(Intent(this@ConfiguracoesActivity, PerfilActivity::class.java))
        }




    }



    private fun getData(){
        var uid = FirebaseAuth.getInstance().uid

        var ref = FirebaseDatabase.getInstance().getReference("/usuarios/$uid")


        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var usuario = snapshot.getValue(Usuario::class.java)!!


                if(usuario?.nome == "" || usuario?.nome == null){

                    binding.nomePerfil.text == "Nome"

                }else{
                    binding.nomePerfil.text = usuario?.nome
                }

                if (usuario?.profileImageUrl == "" || usuario?.profileImageUrl == null) {

                    binding.ImagemPerfil.setImageResource(R.mipmap.ic_launcher)

                } else {
                    Picasso.get()
                        .load(usuario.profileImageUrl)
                        .into(binding.ImagemPerfil)
                }

                if(usuario?.status == "" || usuario?.status == null){

                    binding.StatusPerfil.setText(R.string.status_padrao)
                }else{

                    binding.StatusPerfil.text = usuario?.status
                }



            }


            override fun onCancelled(error: DatabaseError) {
                Log.d(
                    "Config",
                    "Não foi recuperar os dados"
                )
            }
        })


    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}

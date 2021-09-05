package com.app.clonewhatsapp.ui.perfil


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.databinding.ActivityPerfilBinding
import com.app.clonewhatsapp.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso



class PerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding
    private lateinit var toolbar: Toolbar

    lateinit var storage: FirebaseStorage
    lateinit var auth: FirebaseAuth
    lateinit var dataBase: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbarPrincipal)
        toolbar.title = "Perfil"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        getData()

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        dataBase = FirebaseDatabase.getInstance().reference

        binding.LayoutNome.setOnClickListener {
            val bottomSheetNomeFragment = BottomSheetNomeFragment()
            bottomSheetNomeFragment.show(supportFragmentManager,"BottomSheetNome")
        }


        binding.fabFotoPerfil.setOnClickListener {

            val bottomSheet = BottomSheetImagem()
            bottomSheet.show(supportFragmentManager,"BottomSheetDialog")


        }


    }

    override fun onPause() {
        super.onPause()
        getData()
    }

    private fun getData(){
        val uid2 = FirebaseAuth.getInstance().uid
        val ref2 = FirebaseDatabase.getInstance().getReference("/usuarios/$uid2")


        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val usuario = snapshot.getValue(Usuario::class.java)!!


                if (usuario.profileImageUrl == "") {

                    binding.ImagemPerfil.setImageResource(R.mipmap.ic_launcher)

                } else {
                    Picasso.get()
                        .load(usuario.profileImageUrl)
                        .into(binding.ImagemPerfil)
                }

                if (usuario.numero == ""){
                    binding.EditTextTelefone.setText("+55 88 1111 1111")
                }else{
                    binding.EditTextTelefone.setText(usuario.numero)
                }

                if (usuario.nome == ""){
                    binding.editTextNome.setText("Nome")
                }else{
                    binding.editTextNome.setText(usuario.nome)
                }


                if(usuario.status == ""){

                    binding.editTextRecado.setText(R.string.status_padrao)
                }else{
                    binding.editTextRecado.setText(usuario.status)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("PerfilActivity",
                    "Nao foi possivel receber os dados "
                )
            }

        })
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



}


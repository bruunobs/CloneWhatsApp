package com.app.clonewhatsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.Menu
import androidx.appcompat.widget.SearchView

import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.clonewhatsapp.adapter.ContatosAdapter
import com.app.clonewhatsapp.databinding.ActivityContatosBinding
import com.app.clonewhatsapp.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class ContatosActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    lateinit var binding: ActivityContatosBinding

    private var recyclerView: RecyclerView? = null
    private var contatosAdapter: ContatosAdapter? = null
    private var usuarios: List<Usuario>? = null
    private var valueEventListenerContatos: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContatosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerViewListaContatos
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        usuarios = ArrayList()


        toolbar = findViewById(R.id.toolbarPrincipal)
        toolbar.title = ""
        setSupportActionBar(toolbar)

        binding.backArrowContatos.setOnClickListener {
            onBackPressed()
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
        recuperarUsuarios()
    }

    override fun onStop() {
        super.onStop()
        var userref = FirebaseDatabase.getInstance().getReference("/usuarios")
        userref.removeEventListener(valueEventListenerContatos!!)
    }

    //Configuração Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contatos,menu)


        val item = menu!!.findItem(R.id.menuPesquisa)
        val searchView = item.actionView as SearchView
        searchView.queryHint = "Pesquisar..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                //contatosAdapter!!.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                contatosAdapter!!.filter.filter(newText)


                return true
            }

        })


        return super.onCreateOptionsMenu(menu)

    }



    private fun recuperarUsuarios() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        var ref = FirebaseDatabase.getInstance().getReference("/usuarios/")

        valueEventListenerContatos = ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                (usuarios as ArrayList<Usuario>).clear()
                for(snapshot in snapshot.children)
                {
                    var usuario = snapshot.getValue(Usuario::class.java)!!

                    if(!(usuario!!.uid).equals(uid))
                    {
                        (usuarios as ArrayList<Usuario>).add(usuario)
                    }

                }
                contatosAdapter = ContatosAdapter(this@ContatosActivity,usuarios!!,false)
                recyclerView!!.adapter = contatosAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }



}




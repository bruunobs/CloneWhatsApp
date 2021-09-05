package com.app.clonewhatsapp.ui.contatos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView

import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.adapter.ContatosAdapter
import com.app.clonewhatsapp.databinding.ActivityContatosBinding
import com.app.clonewhatsapp.model.Usuario
import com.app.clonewhatsapp.ui.chat.ChatActivity
import com.app.clonewhatsapp.ui.login.LoginActivity
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
        toolbar.title = "Contatos"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


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
//
//                contatosAdapter!!.setOnItemClickListener(object : ContatosAdapter.onItemClickListener{
//                    override fun onItemClick(position: Int) {
//
////                        var listaContatos = snapshot.getValue(Usuario::class.java)!!
////                        val intent =
////                            Intent(this@ContatosActivity, ChatActivity::class.java)
////                        intent.flags =
////                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
////                        intent.putExtra("chatContato",listaContatos.nome)
////                        //intent.putExtra("email_id", ref)
////                        startActivity(intent)
////                        finish()
//                        startActivity(Intent(this@ContatosActivity,ChatActivity::class.java))
//                        finish()
//                    }
//
//                })

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



}




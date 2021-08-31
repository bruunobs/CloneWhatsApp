package com.app.clonewhatsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView

import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.clonewhatsapp.adapter.ContatosAdapter
import com.app.clonewhatsapp.databinding.ActivityCadastroBinding
import com.app.clonewhatsapp.databinding.ActivityConfiguracoesBinding
import com.app.clonewhatsapp.databinding.ActivityContatosBinding
import com.app.clonewhatsapp.fragment.BuscaFragment
import com.app.clonewhatsapp.model.Usuario
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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

//        buscaEditText = findViewById(R.id.buscaContatos)
//
//        buscaEditText!!.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onTextChanged(cs: CharSequence?, start: Int, before: Int, count: Int) {
//                BuscaUsuarios(cs.toString().toLowerCase())
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                TODO("Not yet implemented")
//            }
//
//        })

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
        //searchView.queryHint = "Pesquisar..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//
//            R.id.menuPesquisa -> {
//

//                var bottomSheet = BuscaFragment()
//                bottomSheet.show(supportFragmentManager,"BottomSheetDialog")
//                var buscaFragment = BuscaFragment()
//                supportFragmentManager.beginTransaction().add(R.id.contatos,buscaFragment)
//                    .commit()
//                var buscaFragment = BuscaFragment()
//                buscaFragment.startActivity(this,BuscaFragment::class.java)

//            }
//
//
//        }
        return super.onOptionsItemSelected(item)
    }

    private fun recuperarUsuarios() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        var ref = FirebaseDatabase.getInstance().getReference("/usuarios")

        valueEventListenerContatos = ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                (usuarios as ArrayList<Usuario>).clear()
                for(snapshot in snapshot.children)
                {
                    var usuario: Usuario? = snapshot.getValue(Usuario::class.java)!!
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

    private fun BuscaUsuarios(str: String){
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        var queryUsuarios = FirebaseDatabase.getInstance().getReference("/usuarios").orderByChild("nome")
            .startAt(str).endAt(str + "\uf8ff")

        queryUsuarios.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (usuarios as ArrayList<Usuario>).clear()

                for(snapshot in snapshot.children)
                {
                    var usuario: Usuario? = snapshot.getValue(Usuario::class.java)!!

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
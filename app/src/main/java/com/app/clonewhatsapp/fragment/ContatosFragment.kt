package com.app.clonewhatsapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.clonewhatsapp.ConfiguracoesActivity
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.adapter.ContatosAdapter
import com.app.clonewhatsapp.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ContatosFragment : Fragment() {

    lateinit var toolbar: Toolbar
    private var recyclerView: RecyclerView? = null
    private var contatosAdapter: ContatosAdapter? = null
    private var usuarios: List<Usuario>? = null
    private var valueEventListenerContatos: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_contatos, container, false)

        toolbar = view?.findViewById(R.id.toolbarPrincipal)!!
        toolbar.title = ""
        toolbar.inflateMenu(R.menu.menu_contatos)


//        toolbar.setOnMenuItemClickListener {
//            when(it.itemId){
////                    R.id.menuPesquisa -> {
//////                        var buscaFragment = BuscaFragment()
//////                        supportFragmentManager.beginTransaction().add(R.id.contatos,buscaFragment)
//////                            .commit()
////                    }
////                var buscaFragment = BuscaFragment()
////                buscaFragment.startActivity(this,BuscaFragment::class.java)
//
//            }
//            true
//        }

        recyclerView = view.findViewById(R.id.recyclerViewListaContatos)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        usuarios = ArrayList()

//        view.findViewById<ImageView>(R.id.back_arrow_contatos).setOnClickListener {
//
//            val conversasFragment: Fragment = ConversasFragment()
//            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
//            transaction.replace(R.id.fragment_contatos,
//                conversasFragment
//            ) // give your fragment container id in first parameter
//            transaction.addToBackStack(null) // if written, this transaction will be added to backstack
//            transaction.commit()
//        }


        return view


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
                    contatosAdapter = ContatosAdapter(context!!,usuarios!!,false)
                    recyclerView!!.adapter = contatosAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

}
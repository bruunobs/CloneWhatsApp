package com.app.clonewhatsapp.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.adapter.ContatosAdapter
import com.app.clonewhatsapp.model.Usuario
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BuscaFragment : BottomSheetDialogFragment() {

    private var contatosAdapter: ContatosAdapter? = null
    private var usuarios: List<Usuario>? = null
    private var recyclerView: RecyclerView? = null
    private var buscaEditText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =  inflater.inflate(R.layout.fragment_busca, container, false)

        recyclerView = view.findViewById(R.id.recyclerBuscaContatos)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        buscaEditText = view.findViewById(R.id.buscaContatos)

        usuarios = ArrayList()
        recuperarUsuarios()

        //val buscaUsuarios = view.findViewById<EditText>(R.id.buscaContatos)

        buscaEditText!!.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(cs: CharSequence?, start: Int, before: Int, count: Int) {
                BuscaUsuarios(cs.toString().toLowerCase())
            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

        })

        return view
    }

    private fun recuperarUsuarios() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        var ref = FirebaseDatabase.getInstance().getReference("/usuarios")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                (usuarios as ArrayList<Usuario>).clear()
                if (buscaEditText!!.toString() == ""){

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
                contatosAdapter = ContatosAdapter(context!!,usuarios!!,false)
                recyclerView!!.adapter = contatosAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }


}
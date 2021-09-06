package com.app.clonewhatsapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.clonewhatsapp.adapter.ChatAdapter
import com.app.clonewhatsapp.adapter.ContatosAdapter
import com.app.clonewhatsapp.adapter.ConversasAdapter
import com.app.clonewhatsapp.databinding.FragmentConversasBinding
import com.app.clonewhatsapp.model.Chat
import com.app.clonewhatsapp.model.Conversas
import com.app.clonewhatsapp.model.Usuario
import com.app.clonewhatsapp.ui.contatos.ContatosActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.NonCancellable.children


class ConversasFragment : Fragment() {

    lateinit var binding: FragmentConversasBinding

    private var conversasContatos: ArrayList<Conversas>? = null
    private var recyclerView: RecyclerView? = null
    private var conversasAdapter: ConversasAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentConversasBinding.inflate(inflater, container, false)

        // Configurando RecyclerView
        recyclerView = binding.RecyclerViewConversas
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        conversasContatos = ArrayList()


        Conversas()

        binding.fabContatos.setOnClickListener {
            val intent = Intent(context, ContatosActivity::class.java)
            startActivity(intent)

//            val contatosFragment: Fragment = ContatosFragment()
//            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
//            transaction.replace(R.id.fragment_conversas,
//                contatosFragment
//            ) // give your fragment container id in first parameter
//            transaction.addToBackStack(null) // if written, this transaction will be added to backstack
//            transaction.commit()

        }

        return binding.root


    }

    val ultimaMensagemMap = HashMap<String, Conversas>()

//    private fun refreshRecyclerViewConversas(){
//        (conversasContatos as ArrayList<Conversas>).clear()
//        ultimaMensagemMap.values.forEach {
//            (conversasContatos as ArrayList<Conversas>).add(it)
//        }
//    }

    private fun Conversas(){
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        var intent = Intent()
        val destinatarioId = intent.getStringExtra("contatoID")

        val remetenteId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/conversas-usuarios/$remetenteId")

        ref!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                (conversasContatos as ArrayList<Conversas>).clear()
                for (snapshot in snapshot.children){
                        var conversas = snapshot.getValue(Conversas::class.java)
                    if(conversas!!.remetenteId.equals(remetenteId))
                        {
                            (conversasContatos as ArrayList<Conversas>).add(conversas!!)
                        }

//                    if(!(conversas!!.destinaratioId).equals(destinatarioId))
//                    {
//                        (conversasContatos as ArrayList<Conversas>).add(conversas!!)
//                    }

                }


                conversasAdapter = ConversasAdapter(activity!!,conversasContatos!!,false)
                recyclerView!!.adapter = conversasAdapter


            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                (conversasContatos as ArrayList<Conversas>).clear()
                for (snapshot in snapshot.children){
                    var conversas = snapshot.getValue(Conversas::class.java)
                    if(conversas!!.remetenteId.equals(remetenteId))
                    {
                        (conversasContatos as ArrayList<Conversas>).add(conversas!!)
                    }

                }
                conversasAdapter = ConversasAdapter(activity!!,conversasContatos!!,false)
                recyclerView!!.adapter = conversasAdapter

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })



    }


}
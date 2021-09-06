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
import com.app.clonewhatsapp.adapter.ConversasAdapter
import com.app.clonewhatsapp.databinding.FragmentConversasBinding
import com.app.clonewhatsapp.model.Chat
import com.app.clonewhatsapp.model.Conversas
import com.app.clonewhatsapp.ui.contatos.ContatosActivity


class ConversasFragment : Fragment() {

    lateinit var binding: FragmentConversasBinding

    private var conversas: ArrayList<Conversas>? = null
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
        conversas = ArrayList()

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



}
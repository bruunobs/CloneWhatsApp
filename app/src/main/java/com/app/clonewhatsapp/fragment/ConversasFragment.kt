package com.app.clonewhatsapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.clonewhatsapp.ContatosActivity
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.databinding.FragmentConversasBinding


class ConversasFragment : Fragment() {

    lateinit var binding: FragmentConversasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_conversas, container, false)

        binding = FragmentConversasBinding.inflate(inflater, container, false)

        binding.fabContatos.setOnClickListener {
            val intent = Intent(requireContext(), ContatosActivity::class.java)
            startActivity(intent)

        }

        return binding.root



    }


}
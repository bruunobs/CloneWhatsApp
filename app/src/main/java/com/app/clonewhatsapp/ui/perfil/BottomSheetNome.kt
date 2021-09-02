package com.app.clonewhatsapp.ui.perfil

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.databinding.BottomSheetNomeBinding
import com.app.clonewhatsapp.model.Usuario
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BottomSheetNomeFragment : BottomSheetDialogFragment(){

    private lateinit var binding: BottomSheetNomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = BottomSheetNomeBinding.inflate(inflater,container,false)

        binding.buttonSalvar.setOnClickListener {
            saveUserToFirebaseDatabase()
            dismiss()
        }

        binding.buttonCancelar.setOnClickListener {

            dismiss()
        }

        return binding.root
    }

    private fun saveUserToFirebaseDatabase() {

        var uid = FirebaseAuth.getInstance().uid

        var ref = FirebaseDatabase.getInstance().getReference("/usuarios/$uid")


        ref.child("nome").setValue(binding.EditTextNomePerfil.text.toString())

    }


}
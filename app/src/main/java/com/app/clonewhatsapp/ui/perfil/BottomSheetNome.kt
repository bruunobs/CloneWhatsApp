package com.app.clonewhatsapp.ui.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.databinding.BottomSheetNomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


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
            dismiss()
        }

        binding.buttonCancelar.setOnClickListener {
            dismiss()
        }

        return binding.root
    }


}
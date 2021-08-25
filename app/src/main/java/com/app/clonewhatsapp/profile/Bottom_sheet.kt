package com.app.clonewhatsapp.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.databinding.BottomSheetPickBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Bottom_sheet : BottomSheetDialogFragment(){

    lateinit var binding: BottomSheetPickBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.bottom_sheet_pick,container,false)

        binding = BottomSheetPickBinding.inflate(inflater,container,false)

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ImagemCamera.setOnClickListener {

        }

        binding.ImagemGaleria.setOnClickListener {

        }


    }

}
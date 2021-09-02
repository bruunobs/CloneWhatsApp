package com.app.clonewhatsapp.ui.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.clonewhatsapp.databinding.BottomSheetPickBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.content.Intent
import com.karumi.dexter.Dexter
import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


class BottomSheetImagem : BottomSheetDialogFragment(){

    lateinit var binding: BottomSheetPickBinding

    companion object{
        const val CAMERA_REQUEST_CODE = 777
    }

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
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            if(intent.resolveActivity(requireActivity().packageManager) != null){
//                startActivityForResult(intent, CAMERA_REQUEST_CODE)
//            }else{
//                Log.d("PerfilActivity", "Não foi possivel abrir a camera")
//            }
        }


            binding.ImagemGaleria.setOnClickListener {

            }


        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)


        }





}
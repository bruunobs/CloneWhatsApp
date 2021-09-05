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
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.clonewhatsapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.picasso.Picasso
import okhttp3.internal.Util
import java.io.ByteArrayOutputStream
import java.io.File


class BottomSheetImagem : BottomSheetDialogFragment(){

    lateinit var binding: BottomSheetPickBinding
    lateinit var imageUri: Uri

    lateinit var storage: FirebaseStorage
    lateinit var auth: FirebaseAuth
    lateinit var dataBase: DatabaseReference


    companion object {
        private const val IMAGE_REQUEST = 1
        private const val CAMERA_REQUEST = 777

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = BottomSheetPickBinding.inflate(inflater,container,false)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        dataBase = FirebaseDatabase.getInstance().reference

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.ImagemCamera.setOnClickListener {

            openCamera()

        }


            binding.ImagemGaleria.setOnClickListener {
                openGaleria()


            }


        }

    // ----------------------- Metodos de Click  ------------------------

    private fun openCamera(){

        Dexter.withContext(activity).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            //Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.let {
                    if(report.areAllPermissionsGranted()){
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(intent, CAMERA_REQUEST)

                    }
                }

            }
            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                showRationalDialogForPermissions()
            }

        }

        ).onSameThread().check()


    }

    fun openGaleria() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_REQUEST)

        Dexter.withContext(activity).withPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                val intentGaleria = Intent(Intent.ACTION_PICK, MediaStore
                    .Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intentGaleria, IMAGE_REQUEST)

            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(activity,"Você não permitiu o acesso a galeria",
                    Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?,
                                                            p1: PermissionToken?) {
                showRationalDialogForPermissions()
            }

        }).onSameThread().check()


    }


    // ------------------------ Dialog das permissões -----------------------
    private fun showRationalDialogForPermissions(){
        androidx.appcompat.app.AlertDialog.Builder(requireContext()).setMessage("Você deve aceitar as permissões" +
                "requeridas para poder usar a camera.").setPositiveButton("Va para configuracoes")
        {_,_ ->
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package",activity?.packageName,null)
                intent.data = uri
                startActivity(intent)
            }catch (e: ActivityNotFoundException){
                e.printStackTrace()
            }

        }
            .setNegativeButton("Cancelar")
            {dialog, _ ->
                dialog.dismiss()
            }.show()

    }

    // --------------------   Upload de Imagem------------------------------
    private fun uploadImageGallery() {

        var ref = FirebaseStorage.getInstance()
            .getReference("imagens/${auth.currentUser?.uid}/perfil.jpg")

        //Da upload na imagem para o Storage e Mostra em ImagemPerfil
        ref.putFile(imageUri!!).addOnSuccessListener {


            Log.d("PerfilActivity", "Imagem salva com sucesso: ${it.metadata?.path}")

            //Pega o URL da Imagem e envia para saveUserFirebaseDatabase
            ref.downloadUrl.addOnSuccessListener {

                Log.d("PerfilActivity", "File location: $it")
                saveImagetoDatabase(it.toString())


            }


        }

    }


    // -------------------------- Salva Imagem no Banco de Dados ---------------
    private fun saveImagetoDatabase(profileImageUrl: String) {
        var uid = FirebaseAuth.getInstance().uid
        var ref = FirebaseDatabase.getInstance().getReference("/usuarios/$uid")
        ref.child("profileImageUrl").setValue(profileImageUrl)

        dismiss()

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Galeria
        if (requestCode == IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK &&
            data != null && data?.data!! != null
        ) {
            imageUri = data?.data!!
            uploadImageGallery()

        }

        //Camera
        if (resultCode == AppCompatActivity.RESULT_OK
            && requestCode == CAMERA_REQUEST)
            {

                //Upload Imagem da Camera para o Firebase e mostra na Imagem do Perfil

                val bitmap : Bitmap = data?.extras!!.get("data") as Bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
                val data = baos.toByteArray()

                var ref = FirebaseStorage.getInstance()
                    .getReference("imagens/${auth.currentUser?.uid}/perfil.jpg")

                var uploadTask = ref.putBytes(data)


                uploadTask.addOnSuccessListener {

                    Log.d("Imagem", "Imagem salva com sucesso: ${it.metadata?.path}")

                }
                //Pega o URL da Imagem e manda para saveImagetoDatabase
                ref.downloadUrl.addOnSuccessListener {

                    Log.d("PerfilActivity", "File location: $it")
                    saveImagetoDatabase(it.toString())

                }

            }


    }




}



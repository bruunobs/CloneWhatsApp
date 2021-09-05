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
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
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

            }


        }

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

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        var uid = FirebaseAuth.getInstance().uid
        var ref = FirebaseDatabase.getInstance().getReference("/usuarios/$uid")
        ref.child("profileImageUrl").setValue(profileImageUrl)

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK &&
            data != null && data?.data!! != null
        ) {
            imageUri = data?.data!!
//            uploadImage()

        }

        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == CAMERA_REQUEST) {

                //Upload Imagem da Camera para o Firebase e mostra na Imagem do Perfil
                val bitmap : Bitmap = data?.extras!!.get("data") as Bitmap

               // view.findViewById<ImageView>(R.id.Imagem_Perfil).setImageURI(bitmap)

                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
                val data = baos.toByteArray()

                var ref = FirebaseStorage.getInstance()
                    .getReference("imagens/${auth.currentUser?.uid}/perfil.jpg")

                var uploadTask = ref.putBytes(data)


                uploadTask.addOnSuccessListener {

                    Toast.makeText(activity,"Sucesso ao realizar upload da imagem",Toast.LENGTH_SHORT).show()
                    Log.d("Imagem", "Imagem salva com sucesso: ${it.metadata?.path}")
                   // requireView().findViewById<ImageView>(R.id.Imagem_Perfil).setImageBitmap(bitmap)
//                    Picasso.get()
//                .load(imageUri)
//                .into(binding.ImagemPerfil)


                }

                ref.downloadUrl.addOnSuccessListener {
//                    val intent = Intent(activity,PerfilActivity::class.java)
//                    intent.putExtra("imagem",it)
//                    startActivity(intent)

//                    Picasso.get()
//                .load(it)
//                .into(view?.findViewById(R.id.Imagem_Perfil))
                    Log.d("PerfilActivity", "File location: $it")
                    saveUserToFirebaseDatabase(it.toString())

                }




        }


    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "perfil", null)
        return Uri.parse(path)
    }


}



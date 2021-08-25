package com.app.clonewhatsapp.profile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.databinding.ActivityPerfilBinding
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class PerfilActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPerfilBinding
    private lateinit var toolbar: Toolbar
    lateinit var ImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbarPrincipal)
        toolbar.title = ""
        setSupportActionBar(toolbar)

        binding.backArrowPerfil.setOnClickListener {
            onBackPressed()
        }

        
        val bottomSheet = Bottom_sheet()

        // Seleciona Imagem na galeria
        val getImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                binding.ImagemPerfil.setImageURI(it)
                //uploadImageToFirebaseStorage()

            }
        )

        binding.fabFotoPerfil.setOnClickListener {
            getImage.launch("image/*")
            //bottomSheet.show(supportFragmentManager,"BottomSheetDialog")

        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == 100 && resultCode == RESULT_OK){
//            val imageUri = data?.data!!
//            binding.ImagemPerfil.setImageURI(imageUri)
//            //uploadImageToFirebaseStorage()
//        }
//
//    }



//    private fun uploadImageToFirebaseStorage(){
//
//        //cria uma string para a imagem
//        val filename = UUID.randomUUID().toString()
//        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
//
//        ref.putFile(ImageUri).addOnSuccessListener {
//            binding.ImagemPerfil.setImageURI(null)
//            Log.d("PerfilActivity", "Imagem salva com sucesso: ${it.metadata?.path}")
//
//        }
//    }
}
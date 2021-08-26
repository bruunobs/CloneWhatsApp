package com.app.clonewhatsapp.profile

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.icu.number.NumberFormatter.with
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.databinding.ActivityPerfilBinding
import com.app.clonewhatsapp.model.Usuario
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.internal.Util
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.ObjectInput
import java.util.*
import java.util.concurrent.Executor
import kotlin.collections.HashMap

class PerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding
    private lateinit var toolbar: Toolbar
    lateinit var imageUri: Uri


    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    private lateinit var reference: DatabaseReference
    lateinit var fuser: FirebaseUser


    companion object {
        const val IMAGE_REQUEST = 1

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbarPrincipal)
        toolbar.title = ""
        setSupportActionBar(toolbar)

        storage = FirebaseStorage.getInstance()

        //val storageReference = FirebaseStorage.getInstance().getReference("uploads")
        //val fuser = FirebaseAuth.getInstance().currentUser
        //val reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser?.uid ?: "")

        binding.backArrowPerfil.setOnClickListener {
            onBackPressed()
        }

        binding.fabFotoPerfil.setOnClickListener {
            openImage()
            //getImage.launch("image/*")
            //bottomSheet.show(supportFragmentManager,"BottomSheetDialog")
        }

        //val bottomSheet = Bottom_sheet()

        // Seleciona Imagem na galeria
//        val getImage = registerForActivityResult(
//            ActivityResultContracts.GetContent(),
//            ActivityResultCallback {
//                binding.ImagemPerfil.setImageURI(it)
//
//
//            }
//        )

//        reference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                //val uid = FirebaseAuth.getInstance().uid ?: ""
//                val usuario: Usuario? = snapshot.getValue(Usuario::class.java)
//                //val usuario = Usuario(uid, nomeUsuario, )
//                //val usuario = Usuario(uid,nomeUsuario.text.toString(),profileImageUrl).sn
//
//
//                binding.editTextNome.setText(usuario?.nome)
//                if (usuario?.profileImageUrl.equals("default")) {
//                    binding.ImagemPerfil.setImageResource(R.mipmap.ic_launcher)
//                } else {
//                        Picasso.get()
//                        .load(usuario?.profileImageUrl)
//                        .into(binding.ImagemPerfil)
//
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.d("PerfilActivity","Não foi possivel salvar imagem")
//            }
//        })


    }

    private fun getFileExtension(uri: Uri?): String? {
        val contentResolver: ContentResolver = baseContext.contentResolver
        val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri!!))
    }




    // ----------------------- Metodos de Click  ------------------------

    fun openImage() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, IMAGE_REQUEST)
    }


    // ------------------------  Bottom Sheet ----------------------------------


    // bottomSheet.show(supportFragmentManager,"BottomSheetDialog")


    // --------------------   Upload de Imagem------------------------------
    private fun uploadImage() {

        if (imageUri == null) return

            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

            ref.putFile(imageUri!!).addOnSuccessListener {

                binding.ImagemPerfil.setImageURI(null)
                Log.d("PerfilActivity", "Imagem salva com sucesso: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("PerfilActivity", "File location: $it")

                    saveUserToFirebaseDatabase(it.toString())
                }
            }




    }

    private fun uploadImageToFirebaseStorage() {
        if (imageUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(imageUri!!).addOnSuccessListener {

            binding.ImagemPerfil.setImageURI(null)
            Log.d("PerfilActivity", "Imagem salva com sucesso: ${it.metadata?.path}")

            ref.downloadUrl.addOnSuccessListener {
                Log.d("PerfilActivity", "File location: $it")

                saveUserToFirebaseDatabase(it.toString())
            }
        }
    }

    // -------------------------- Salvar Usuario e Imagem ------------------------------

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {

        //val uid = FirebaseAuth.getInstance().uid ?: ""

        val uid = UUID.randomUUID().toString()

        val ref = FirebaseDatabase.getInstance().getReference("/usuarios/$uid")

        var usuario = Usuario(uid, binding.editTextNome.text.toString(), profileImageUrl)


        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                usuario = snapshot.getValue(Usuario::class.java)!!
                binding.editTextNome.setText(usuario?.nome)

                if (usuario?.profileImageUrl == "default") {
                    binding.ImagemPerfil.setImageResource(R.mipmap.ic_launcher)
                } else {
                        Picasso.get()
                        .load(usuario.profileImageUrl)
                        .into(binding.ImagemPerfil)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("PerfilActivity","Não foi possivel salvar imagem e usuario no Banco de Dados")
            }

        })

            ref.setValue(usuario)
            .addOnSuccessListener {

                Log.d("PerfilActivity", "Usuario e Imagem Salvas no Banco de dados")
            }.addOnFailureListener() {

                Log.d("PerfilActivity", "Erro ao salvar usuario  no banco de dados")
            }
    }





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK &&
            data != null && data?.data!! != null
        ) {
            imageUri = data?.data!!
            //binding.ImagemPerfil.setImageURI(imageUri)
            uploadImage()

            //val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            //binding.ImagemPerfil.setImageBitmap(bitmap)
            //binding.editTextNome.setText(usuario?.nome)
            //uploadImageToFirebaseStorage()

        }

    }



}





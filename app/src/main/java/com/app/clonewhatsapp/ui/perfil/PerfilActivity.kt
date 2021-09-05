package com.app.clonewhatsapp.ui.perfil

import android.content.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.databinding.ActivityPerfilBinding
import com.app.clonewhatsapp.model.Usuario
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import android.Manifest
import android.graphics.Bitmap
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.*
import java.util.*


class PerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding
    private lateinit var toolbar: Toolbar
    lateinit var imageUri: Uri

    lateinit var storage: FirebaseStorage
    lateinit var auth: FirebaseAuth
    lateinit var dataBase: DatabaseReference

    companion object {
        private const val IMAGE_REQUEST = 1
        private const val CAMERA_REQUEST = 777

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbarPrincipal)
        toolbar.title = "Perfil"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        getData()
        //var bottomSheet  = Bottom_sheet()

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        dataBase = FirebaseDatabase.getInstance().reference

        binding.LayoutNome.setOnClickListener {
            val bottomSheetNomeFragment = BottomSheetNomeFragment()
            bottomSheetNomeFragment.show(supportFragmentManager,"BottomSheetNome")
        }


        binding.fabFotoPerfil.setOnClickListener {

            var bottomSheet = BottomSheetImagem()
            bottomSheet.show(supportFragmentManager,"BottomSheetDialog")
            //showBottomSheetPickPhoto()
//            val tirarFoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//
//            if (tirarFoto.resolveActivity(this.packageManager) != null){
//                startActivityForResult(tirarFoto, CAMERA_REQUEST)
//            }else{
//                Toast.makeText(this, "Erro ao abrir a camera", Toast.LENGTH_SHORT).show()
//            }

        }


    }



    // ----------------------- Metodos de Click  ------------------------

    fun openGaleria() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_REQUEST)
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(intent, IMAGE_REQUEST)

        Dexter.withContext(this@PerfilActivity).withPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                val intentGaleria = Intent(Intent.ACTION_PICK, MediaStore
                    .Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intentGaleria, IMAGE_REQUEST)

            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(this@PerfilActivity,"Você não permitiu o acesso a galeria",
                    Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?,
                                                            p1: PermissionToken?) {
                showRationalDialogForPermissions()
            }

        }).onSameThread().check()


    }

    private fun openCamera(){

        Dexter.withContext(this).withPermissions(
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

//            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//                takePictureIntent.resolveActivity(packageManager)?.also {
//                    startActivityForResult(takePictureIntent, CAMERA_REQUEST)
//                }
//            }
        }

        ).onSameThread().check()


    }


    // ------------------------  Bottom Sheet ----------------------------------



    private fun showBottomSheetPickPhoto() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_pick, null)

        view.findViewById<View>(R.id.Imagem_Galeria).setOnClickListener {
            openGaleria()
            dialog.dismiss()
        }

        view.findViewById<View>(R.id.Imagem_Camera).setOnClickListener {
            openCamera()
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }


    // --------------------   Upload de Imagem------------------------------
    private fun uploadImage() {

        //val filename = UUID.randomUUID().toString() //$filename

        var ref = FirebaseStorage.getInstance()
            .getReference("imagens/${auth.currentUser?.uid}/perfil.jpg")

        //Da upload na imagem para o Storage e Mostra em ImagemPerfil
        ref.putFile(imageUri!!).addOnSuccessListener {

            Picasso.get()
                .load(imageUri)
                .into(binding.ImagemPerfil)

            Log.d("PerfilActivity", "Imagem salva com sucesso: ${it.metadata?.path}")

            //Pega o URL da Imagem e envia para saveUserFirebaseDatabase
            ref.downloadUrl.addOnSuccessListener {

                Log.d("PerfilActivity", "File location: $it")
                saveUserToFirebaseDatabase(it.toString())


            }


        }

    }


    // -------------------------- Salva Usuario no Banco de Dados ------------------------------

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {

        var uid = FirebaseAuth.getInstance().uid
        //val uid = UUID.randomUUID().toString()

        var usuario = Usuario(uid!!, binding.editTextNome.text.toString(),
            profileImageUrl, binding.EditTextTelefone.text.toString(),binding.editTextRecado.text.toString())

        var ref = FirebaseDatabase.getInstance().getReference("/usuarios/$uid")


        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                usuario = snapshot.getValue(Usuario::class.java)!!

                binding.editTextNome.setText(usuario?.nome)
                binding.EditTextTelefone.setText(usuario?.numero)
                binding.editTextRecado.setText(usuario?.status)


            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(
                    "PerfilActivity",
                    "Não foi possivel salvar usuario no Banco de Dados"
                )
            }

        })


        //Só deus sabe o que isso faz
        ref.setValue(usuario)
            .addOnSuccessListener {

                Log.d("PerfilActivity", "Usuario Salvo")
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
            uploadImage()

        }

       if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {

                data?.extras?.let {
                    val bitmap : Bitmap = data.extras!!.get("data") as Bitmap
                    binding.ImagemPerfil.setImageBitmap(bitmap)
                    //uploadImage()
                }


        }


    }

    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()
//        val intent = Intent()
//        intent.extras?.getString("imagem")
//        Picasso.get()
//            .load("imagem")
//            .into(binding.ImagemPerfil)
    }



    private fun getData(){
        var uid2 = FirebaseAuth.getInstance().uid
        var ref2 = FirebaseDatabase.getInstance().getReference("/usuarios/$uid2")


        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var usuario = snapshot.getValue(Usuario::class.java)!!

                if (usuario?.profileImageUrl == "") {

                    binding.ImagemPerfil.setImageResource(R.mipmap.ic_launcher)

                } else {
                    Picasso.get()
                        .load(usuario.profileImageUrl)
                        .into(binding.ImagemPerfil)
                }

                if (usuario?.numero == ""){
                    binding.EditTextTelefone.setText("+55 88 1111 1111")
                }else{
                    binding.EditTextTelefone.setText(usuario?.numero)
                }

                if (usuario?.nome == ""){
                    binding.editTextNome.setText("Nome")
                }else{
                    binding.editTextNome.setText(usuario?.nome)
                }


                if(usuario?.status == ""){

                    binding.editTextRecado.setText(R.string.status_padrao)
                }else{
                    binding.editTextRecado.setText(usuario?.status)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("PerfilActivity",
                    "Nao foi possivel receber os dados "
                )
            }

        })
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showRationalDialogForPermissions(){
        AlertDialog.Builder(this).setMessage("Voce nao aceitou as permissoes" +
                "requeridas para usar essa feature").setPositiveButton("Va para configuracoes")
        {_,_ ->
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package",packageName,null)
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

}


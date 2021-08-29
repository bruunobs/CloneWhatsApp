package com.app.clonewhatsapp.cadastro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.app.clonewhatsapp.databinding.ActivityCadastroBinding
import com.app.clonewhatsapp.login.LoginActivity
import com.app.clonewhatsapp.model.Usuario
import com.google.firebase.auth.*
import com.google.firebase.database.*
import java.lang.Exception


class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    lateinit var auth: FirebaseAuth
    var dataBase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.textEntrarLogin.setOnClickListener {
            onBackPressed()
        }

        auth = FirebaseAuth.getInstance()
        //dataBase = FirebaseDatabase.getInstance().getReference("/usuarios/")


        binding.buttonCadastrar.setOnClickListener {
            when {


                TextUtils.isEmpty(binding.editNome.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@CadastroActivity,
                        "Preencha o nome.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(binding.editEmail.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@CadastroActivity,
                        "Preencha o email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(binding.editSenha.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@CadastroActivity,
                        "Preencha a senha.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    //Se tiver espaço ele vai remover
                    val nome: String = binding.editNome.text.toString().trim() { it <= ' ' }
                    val email: String = binding.editEmail.text.toString().trim() { it <= ' ' }
                    val senha: String = binding.editSenha.text.toString().trim() { it <= ' ' }

                    //Create an instance and create a register a user with email and password
                    auth.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                //Se o registro for feito com sucesso
                                saveUserToFirebaseDatabase()
                                //val firebaseUser: FirebaseUser = task.result!!.user!!
                                Toast.makeText(
                                    this@CadastroActivity,
                                    "Você se registrou com sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent =
                                    Intent(this@CadastroActivity, LoginActivity::class.java)
                                //intent.flags =
                                    //Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                //intent.putExtra("user_id", firebaseUser.uid)
                                //intent.putExtra("email_id", email)
                                startActivity(intent)
                                finish()


                            } else {
                                var excecao = ""
                                try {
                                    throw task.exception!!
                                } catch (e: FirebaseAuthUserCollisionException) {
                                    excecao = "Essa conta ja foi cadastrada!"
                                } catch (e: FirebaseAuthWeakPasswordException) {
                                    excecao = "Digite uma senha mais forte!"
                                } catch (e: FirebaseAuthInvalidCredentialsException) {
                                    excecao = "Digite um e-mail válido"
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@CadastroActivity, task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                Toast.makeText(this@CadastroActivity, excecao, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                }
            }

        }

    }


    private fun saveUserToFirebaseDatabase() {
        var uid = FirebaseAuth.getInstance().uid


        var ref = FirebaseDatabase.getInstance().getReference("/usuarios/$uid")

        ref.child("nome").setValue(binding.editNome.text.toString())


    }



}
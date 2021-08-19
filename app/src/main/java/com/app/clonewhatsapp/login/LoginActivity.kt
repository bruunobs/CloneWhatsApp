package com.app.clonewhatsapp.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.app.clonewhatsapp.MainActivity
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.cadastro.CadastroActivity
import com.app.clonewhatsapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textCadastrese.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }
        auth = FirebaseAuth.getInstance()

        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

        binding.buttonEntrar.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.editEmail.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Preencha o email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(binding.editSenha.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Preencha a senha.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    //Se tiver espaço ele vai remover

                    val email: String = binding.editEmail.text.toString().trim() { it <= ' ' }
                    val senha: String = binding.editSenha.text.toString().trim() { it <= ' ' }

                    //Logar usando FirebaseAuth
                    auth.signInWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(this@LoginActivity) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@LoginActivity, "Voce teve sucesso ao logar!",Toast.LENGTH_SHORT).show()

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id",auth.currentUser!!.uid)
                                intent.putExtra("email_id", email)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }


}



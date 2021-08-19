package com.app.clonewhatsapp.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.cadastro.CadastroActivity
import com.app.clonewhatsapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textCadastrese.setOnClickListener {
            val intent = Intent(this,CadastroActivity::class.java)
            startActivity(intent)
        }
        auth = FirebaseAuth.getInstance()

        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")
    }

    fun login(email:String, senha:String){
        auth.signInWithEmailAndPassword(email,senha).addOnCompleteListener(this){task ->
            if(task.isSuccessful){

            }else{

            }
        }
    }
}
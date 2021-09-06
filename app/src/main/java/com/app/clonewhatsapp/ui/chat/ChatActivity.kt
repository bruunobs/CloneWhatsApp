package com.app.clonewhatsapp.ui.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.app.clonewhatsapp.R
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.clonewhatsapp.adapter.ChatAdapter
import com.app.clonewhatsapp.adapter.ContatosAdapter
import com.app.clonewhatsapp.databinding.ActivityChatBinding
import com.app.clonewhatsapp.model.Chat
import com.app.clonewhatsapp.model.Usuario
import com.app.clonewhatsapp.ui.principal.PrincipalActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var toolbar: Toolbar
    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null

    private var chatMensagem: ArrayList<Chat>? = null
    private var recyclerView: RecyclerView? = null
    private var chatAdapter: ChatAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurando toolbar
        toolbar = findViewById(R.id.toolbarPrincipal)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Configurando RecyclerView
        recyclerView = binding.RecyclerViewChat
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        chatMensagem = ArrayList()

        //Recebendo Imagem e nome do contato
        var intent = intent
        var contatoId = intent.getStringExtra("contatoID")
        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("/usuarios/").child(contatoId!!)

        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var contato = snapshot.getValue(Usuario::class.java)
                binding.nomeChat.text = contato!!.nome

                if (contato!!.profileImageUrl == "") {

                    binding.ImagemChat.setImageResource(R.mipmap.ic_launcher)

                } else {
                    Picasso.get()
                        .load(contato.profileImageUrl)
                        .into(binding.ImagemChat)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        readMessage(firebaseUser!!.uid,contatoId)

        binding.fabMic.setOnClickListener {
            var mensagem: String = binding.EditTextChat.text.toString()

            if(mensagem.isEmpty()){
                Toast.makeText(applicationContext,"Mensagem esta vazia",Toast.LENGTH_SHORT).show()
                binding.EditTextChat.setText("")
            }else{
                sendMessage(firebaseUser!!.uid,contatoId,mensagem,System.currentTimeMillis() / 1000)
                binding.EditTextChat.setText("")

            }
        }



    }

    //Enviar mensagem
    private fun sendMessage(remetenteId: String, destinatarioId: String, mensagem: String, tempo: Long){
        var reference: DatabaseReference?  = FirebaseDatabase.getInstance().getReference()

        //Salva Mensagem
        val idMessage = UUID.randomUUID().toString() //$idMessage
        var hashMap: HashMap<String,Any> = HashMap()
        hashMap.put("Id", idMessage)
        hashMap.put("remetenteId", remetenteId)
        hashMap.put("destinaratioId", destinatarioId)
        hashMap.put("mensagem", mensagem)
        hashMap.put("tempo", tempo)

        reference!!.child("/mensagens-usuarios/$remetenteId/$destinatarioId").push().setValue(hashMap)
        reference!!.child("/mensagens-usuarios/$destinatarioId/$remetenteId").push().setValue(hashMap)

        //Salva Conversa

    }

    

    // Ler Mensagem
    private fun readMessage(remetenteId: String, destinatarioId: String){

        var ref = FirebaseDatabase.getInstance().getReference("/mensagens-usuarios/$remetenteId/$destinatarioId")


        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                (chatMensagem as ArrayList<Chat>).clear()
                for(snapshot in snapshot.children)
                {
                    var chat = snapshot.getValue(Chat::class.java)!!

                    if(chat!!.remetenteId.equals(remetenteId) && chat!!.destinaratioId.equals(destinatarioId) ||
                        chat!!.remetenteId.equals(destinatarioId) && chat!!.destinaratioId.equals(remetenteId))
                    {
                        (chatMensagem as ArrayList<Chat>).add(chat)
                    }

                }
                chatAdapter = ChatAdapter(this@ChatActivity,chatMensagem!!,false)
                recyclerView!!.adapter = chatAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }


    private fun saveChat(){
        
    }

    override fun onSupportNavigateUp(): Boolean {
        startActivity(Intent(this@ChatActivity,PrincipalActivity::class.java))
        finish()
        return true
    }

}
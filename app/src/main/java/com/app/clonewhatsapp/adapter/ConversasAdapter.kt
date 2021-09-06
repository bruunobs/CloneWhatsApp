package com.app.clonewhatsapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.model.Chat
import com.app.clonewhatsapp.model.Conversas
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView

class ConversasAdapter(mContest: Context, conversasList: ArrayList<Conversas>, isChatCheck: Boolean) : RecyclerView.Adapter<ConversasAdapter.ViewHolder>() {

    private val mContest: Context
    private var conversasList: ArrayList<Conversas>
    private var isChatCheck: Boolean

    var firebaseUser: FirebaseUser? = null

    init {
        this.mContest = mContest
        this.conversasList = conversasList
        this.isChatCheck = isChatCheck

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            val view: View = LayoutInflater.from(mContest).inflate(R.layout.adapter_conversas,parent,false)

            return ConversasAdapter.ViewHolder(view)



    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val conversas: Conversas? = conversasList[i]
        holder.itemView.findViewById<TextView>(R.id.ultima_conversa).text = conversas!!.mensagem
//        holder.itemView.findViewById<TextView>(R.id.nome_conversa).text = conversas!!.destinaratioId


    }

    override fun getItemCount(): Int {

        return conversasList.size
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var UltimaMensagem: TextView
        var imagePerfil: CircleImageView
        var horaMensagem : TextView

        init {
            UltimaMensagem = itemView.findViewById(R.id.ultima_conversa)
            imagePerfil = itemView.findViewById(R.id.imagem_perfil_conversa)
            horaMensagem = itemView.findViewById(R.id.hora_mensagem_conversa)

        }

    }




}
package com.app.clonewhatsapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.model.Chat
import com.app.clonewhatsapp.model.Usuario
import com.app.clonewhatsapp.ui.chat.ChatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.concurrent.TimeUnit

class ChatAdapter(mContest: Context, chatList: ArrayList<Chat>, isChatCheck: Boolean) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val mContest: Context
    private var chatList: ArrayList<Chat>
    private var isChatCheck: Boolean

    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1
    var firebaseUser: FirebaseUser? = null



    init {
        this.mContest = mContest
        this.chatList = chatList
        this.isChatCheck = isChatCheck
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType == MESSAGE_TYPE_RIGHT){
            val view: View = LayoutInflater.from(mContest).inflate(R.layout.adapter_chat_direita,parent,false)

            return ChatAdapter.ViewHolder(view)
        }else{
            val view: View = LayoutInflater.from(mContest).inflate(R.layout.adapter_chat_esquerda,parent,false)

            return ChatAdapter.ViewHolder(view)
        }


    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val chat: Chat? = chatList[i]
        holder.itemView.findViewById<TextView>(R.id.mensagem_chat).text = chat!!.mensagem
        holder.itemView.findViewById<TextView>(R.id.hora_mensagem).text = formatToDigitalClock(chat.tempo!!)

    }

    override fun getItemCount(): Int {

        return chatList.size
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var mensagem: TextView
//        var status: TextView

        init {
            mensagem = itemView.findViewById(R.id.mensagem_chat)
//            nome = itemView.findViewById(R.id.nome_contatos)
            // Colocar o da hora depois

        }

    }

    override fun getItemViewType(position: Int): Int {
       firebaseUser = FirebaseAuth.getInstance().currentUser
        if(chatList[position].remetenteId == firebaseUser!!.uid){
            return  MESSAGE_TYPE_RIGHT
        }else{
            return MESSAGE_TYPE_LEFT
        }
    }

    fun formatToDigitalClock(miliSeconds: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(miliSeconds).toInt() % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(miliSeconds).toInt() % 60
//        val seconds = TimeUnit.MILLISECONDS.toSeconds(miliSeconds).toInt() % 60
        return when {
            hours > 0 -> String.format("%d:%02d", hours, minutes)
            minutes > 0 -> String.format("%02d", minutes)
//            seconds > 0 -> String.format("00:%02d")
            else -> {
                "00:00"
            }
        }
    }


}

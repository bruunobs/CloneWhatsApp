package com.app.clonewhatsapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.model.Conversas
import com.app.clonewhatsapp.model.Usuario
import com.app.clonewhatsapp.ui.chat.ChatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ConversasAdapter(mContest: Context, conversasList: ArrayList<Conversas>) : RecyclerView.Adapter<ConversasAdapter.ViewHolder>(), Filterable {

    private val mContest: Context
    private var conversasList: ArrayList<Conversas>
    private var conversasListFilter: ArrayList<Conversas>

    init {
        this.mContest = mContest
        this.conversasList = conversasList
        this.conversasListFilter = conversasList
        notifyDataSetChanged()


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            val view: View = LayoutInflater.from(mContest).inflate(R.layout.adapter_conversas,parent,false)

            return ConversasAdapter.ViewHolder(view)



    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val conversas: Conversas? = conversasList[i]
        //val conversas = Conversas()
        val chatPartnerId: String
        if (conversas!!.remetenteId == FirebaseAuth.getInstance().uid){
            chatPartnerId = conversas.destinaratioId!!
        }else{
            chatPartnerId = conversas.remetenteId!!
        }

        val ref = FirebaseDatabase.getInstance().getReference("/usuarios/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuario = snapshot.getValue(Usuario::class.java)
                holder.itemView.findViewById<TextView>(R.id.nome_conversa).text = usuario?.nome
                if (usuario?.profileImageUrl == null || usuario?.profileImageUrl == ""){
                    holder.itemView.findViewById<ImageView>(R.id.imagem_perfil_conversa).setImageResource(R.mipmap.ic_launcher)
                }else{
                    Picasso.get()
                        .load(usuario?.profileImageUrl)
                        .into(holder.imagePerfil)
                }

                holder.layoutConversas.setOnClickListener {
                    val intent = Intent(mContest, ChatActivity::class.java)
                    intent.putExtra("contatoID", usuario!!.uid)
                    mContest.startActivity(intent)
                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        holder.itemView.findViewById<TextView>(R.id.ultima_conversa).text = conversas!!.mensagem




    }

    override fun getItemCount(): Int {

        return conversasList.size
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var UltimaMensagem: TextView
        var imagePerfil: CircleImageView
        var horaMensagem : TextView
        var layoutConversas: RelativeLayout

        init {
            UltimaMensagem = itemView.findViewById(R.id.ultima_conversa)
            imagePerfil = itemView.findViewById(R.id.imagem_perfil_conversa)
            horaMensagem = itemView.findViewById(R.id.hora_mensagem_conversa)
            layoutConversas = itemView.findViewById(R.id.adapter_conversas)

        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(cs: CharSequence?): FilterResults {

                val filterResult = FilterResults()
                if(cs == null || cs.length < 0){
                    filterResult.count = conversasListFilter.size
                    filterResult.values = conversasListFilter
                }else{

                    var cs = cs.toString().toLowerCase()
                    val itemModal = ArrayList<Conversas>()

                    for (item in conversasListFilter)
                    {
                        if(item.mensagem!!.toLowerCase().contains(cs.toLowerCase())){
                            itemModal.add(item)
                        }


                    }

                    filterResult.count = itemModal.size
                    filterResult.values = itemModal
                }

                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                conversasList = results!!.values as ArrayList<Conversas>
                notifyDataSetChanged()
            }
        }
    }



}
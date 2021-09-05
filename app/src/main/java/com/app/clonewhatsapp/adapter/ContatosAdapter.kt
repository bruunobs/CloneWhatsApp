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
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.model.Usuario
import com.app.clonewhatsapp.ui.chat.ChatActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ContatosAdapter(mContest: Context,listaContatos: List<Usuario>,isChatCheck: Boolean) : RecyclerView.Adapter<ContatosAdapter.ViewHolder>(), Filterable {

    private val mContest: Context
    private var listaContatos: List<Usuario>
    private var listaContatosFilter: List<Usuario>
    private var isChatCheck: Boolean

    //private lateinit var mListenr: onItemClickListener

    init {
        this.mContest = mContest
        this.listaContatos = listaContatos
        this.listaContatosFilter = listaContatos
        this.isChatCheck = isChatCheck
        notifyDataSetChanged()
    }

//    interface onItemClickListener{
//
//        fun onItemClick(position: Int)
//
//    }
//
//    fun setOnItemClickListener(listener: onItemClickListener){
//
//        mListenr = listener
//
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view: View = LayoutInflater.from(mContest).inflate(R.layout.adapter_contatos,parent,false)

        return ContatosAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val usuario: Usuario? = listaContatos[i]
        holder.itemView.findViewById<TextView>(R.id.nome_contatos).text = usuario!!.nome
        holder.itemView.findViewById<TextView>(R.id.status_contatos).text = usuario!!.status

        Picasso.get()
            .load(usuario.profileImageUrl)
            .into(holder.profileImage)

        holder.layoutUser.setOnClickListener {
            val intent = Intent(mContest, ChatActivity::class.java)
            intent.putExtra("contatoID", usuario.uid)
            mContest.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {

        return listaContatos.size
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var nome: TextView
        var status: TextView
        var profileImage: CircleImageView
        var layoutUser: RelativeLayout

        init {
//            itemView.setOnClickListener {
//                listener.onItemClick(adapterPosition)
//            }
            nome = itemView.findViewById(R.id.nome_contatos)
            status = itemView.findViewById(R.id.status_contatos)
            profileImage = itemView.findViewById(R.id.imagem_perfil_contatos)
            layoutUser = itemView.findViewById(R.id.adapter_contatos)
        }

    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(cs: CharSequence?): FilterResults {
                val filterResult = FilterResults()
                if(cs == null || cs.length < 0){
                    filterResult.count = listaContatosFilter.size
                    filterResult.values = listaContatosFilter
                }else{

                    var cs = cs.toString().toLowerCase()
                    val itemModal = ArrayList<Usuario>()

                    for (item in listaContatosFilter)
                    {
                     if(item.nome!!.toLowerCase().contains(cs.toLowerCase())){
                         itemModal.add(item)
                     }
                    }

                    filterResult.count = itemModal.size
                    filterResult.values = itemModal
                }

            return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listaContatos = results!!.values as ArrayList<Usuario>
                notifyDataSetChanged()
            }

        }
    }

}


package com.app.clonewhatsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.clonewhatsapp.R
import com.app.clonewhatsapp.model.Usuario

class ContatosAdapter(var listaContatos: List<Usuario>) : RecyclerView.Adapter<ViewHolder>() {

    //var listaContatos = ArrayList<Usuario>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.activity_contatos,parent,false)

        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {

        return listaContatos.size
    }


}

class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){

    fun bind(listaContatos: ArrayList<Usuario>){

    }

}
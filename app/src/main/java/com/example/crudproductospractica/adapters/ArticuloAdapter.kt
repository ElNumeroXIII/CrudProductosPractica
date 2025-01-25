package com.example.crudproductospractica.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crudproductospractica.databinding.ItemArticuloBinding
import com.example.crudproductospractica.models.Articulo

class ArticuloAdapter(
    var lista: MutableList<Articulo>,
    private val onEdit: (Articulo) -> Unit,
    private val onDelete: (Articulo) -> Unit
) : RecyclerView.Adapter<ArticuloViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticuloViewHolder {
        val binding = ItemArticuloBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticuloViewHolder(binding)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ArticuloViewHolder, position: Int) {
        holder.render(lista[position], onEdit, onDelete)
    }

    fun updateList(nuevaLista: MutableList<Articulo>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}


package com.example.crudproductospractica.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.crudproductospractica.databinding.ItemArticuloBinding
import com.example.crudproductospractica.models.Articulo

class ArticuloViewHolder(private val binding: ItemArticuloBinding) : RecyclerView.ViewHolder(binding.root) {

    fun render(articulo: Articulo, onEdit: (Articulo) -> Unit, onDelete: (Articulo) -> Unit) {
        binding.tvNombre.text = articulo.nombre
        binding.tvDescripcion.text = articulo.descripcion
        binding.tvPrecio.text = "Precio: $${articulo.precio}"

        binding.btnEdit.setOnClickListener { onEdit(articulo) }
        binding.btnDelete.setOnClickListener { onDelete(articulo) }
    }
}


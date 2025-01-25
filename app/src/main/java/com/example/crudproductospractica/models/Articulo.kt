package com.example.crudproductospractica.models

import java.io.Serializable

data class Articulo(
    val nombre: String = "", // Nombre único del artículo
    val descripcion: String = "", // Descripción del artículo
    val precio: Float = 0F // Precio del artículo
) : Serializable
